package com.fxa.image.store.service;

import com.fxa.image.store.RabbitMqConfig;
import com.fxa.image.store.mapper.ProductMapper;
import com.fxa.image.store.mode.Product;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StoreService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate2;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ScheduledExecutorService scheduledExecutorService;

    @Resource
    StockService stockService;

    @Resource
    RabbitTemplate rabbitTemplate;


    public boolean acquireLock(RedisTemplate<String, Object> redisTemplate, String lockKey, String requestId, long expireTime) {
        // 使用 SETNX 命令尝试获取锁
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
        return result != null && result;
    }

    public void releaseLock(RedisTemplate<String, Object> redisTemplate, String lockKey, String requestId) {
        // 检查当前请求是否持有锁，如果是则释放锁
        Object currentValue = redisTemplate.opsForValue().get(lockKey);
        if (currentValue != null && currentValue.equals(requestId)) {
            redisTemplate.delete(lockKey);
            System.out.println("释放锁:" + lockKey + " " + requestId);
        }
    }

    private final Random mRandom = new Random();

    /**
     * 下单
     * 锁一个商品的库存，一般是下单待支付
     *
     * @return 订单信息
     */
    public long ordered() {
        //这里模拟让80%的请求进入到redis1 20%的请求进入到redis2
        int randomNumber = mRandom.nextInt(100); // 生成0到99的随机数
        String id;
        if (randomNumber < 80) {
            // 将请求发送到redis1
            System.out.println("Send request to redis1");
            int i = mRandom.nextInt(mProductInRedis1.size());
            id = mProductInRedis1.get(i);
            return realOrdered(redisTemplate, id);
        } else {
            // 将请求发送到redis2
            System.out.println("Send request to redis2");
            int i = mRandom.nextInt(mProductInRedis2.size());
            id = mProductInRedis2.get(i);
            return realOrdered(redisTemplate2, id);
        }
    }

    public long realOrdered(RedisTemplate<String, Object> redisTemplate, String id) {
        //创建一把redis锁 过期时间15分钟
        //先检查商品在redis中还存不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(id))) {
            System.out.println("order:" + id + " 商品已经下架");
            return -1;
        }
        boolean locked = acquireLock(redisTemplate, "product_sale_lock_" + id, id, 5 * 60);
        if (!locked) {
            System.out.println("order:" + id + " 来晚了被其他人买了");
            //这里可以给这个请求设置一个标记为代表此人不可再抢
            return -1;
        }
        long orderId = Math.abs(("redis1" + id + System.currentTimeMillis()).hashCode());
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                int i = mRandom.nextInt(100);
                if (i < 75) {
                    completePay(redisTemplate, orderId, id);
                } else {
                    cancel(redisTemplate, orderId, id);
                }
            }
        }, 15, TimeUnit.SECONDS);
        return orderId;
    }

    public void cancel(RedisTemplate<String, Object> redisTemplate, long orderId, String idInRedis) {
        System.out.println("completePay:" + orderId + " 订单取消");
        releaseLock(redisTemplate, "product_sale_lock_" + idInRedis, idInRedis);
    }

    public void completePay(RedisTemplate<String, Object> redisTemplate, long orderId, String idInRedis) {
        System.out.println("completePay:" + orderId + "支付成功，更改订单状态");
        releaseLock(redisTemplate, "product_sale_lock_" + idInRedis, idInRedis);
        //删除redis中的这条商品 同步扣减db中的库存
        boolean delete = Boolean.TRUE.equals(redisTemplate.delete(idInRedis));
        System.out.println("delete:" + delete);
        //扣减mysql中的该商品的 库存
        if (!delete) {
            return;
        }
        String[] keys = idInRedis.split(":");
        int idInDb = Integer.parseInt(keys[keys.length - 2]);
        if (idInDb > 0) {
            //这样写不行 不同的线程同时修改数据库中的数据会出现脏读导致出问题
            //这里的解决方案： 2.使用mq 3.利用数据库的锁机制SELECT ... FOR UPDATE(仍然会导致数据少更新)
            //不能使用分布式锁的方式 获取锁失败的更新线程会失去更新的机会
//            boolean locked = acquireLock(redisTemplate,"product:lock:stock:"+idInDb,idInDb+"",15);
//            if(locked){
            stockService.updateProductStock(idInDb);

//                releaseLock(redisTemplate,"product:lock:stock:"+idInDb,idInDb+"");
//            }else{
//                System.out.println("获取锁失败");
//            }


            //感觉最稳妥的方式是使用mq
//            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,RabbitMqConfig.ROUTINGKEY,idInDb);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        stockService.updateProductStock(Long.parseLong(message));
    }




    List<String> mProductInRedis1 = new ArrayList<>();
    List<String> mProductInRedis2 = new ArrayList<>();

    public void loadToCache() {
        mProductInRedis1.clear();
        mProductInRedis2.clear();
        List<Product> products = productMapper.getAllProducts();
        for (Product product : products) {
            //每个产品的库存量
            int stock = product.getStock();
            //秒杀活动只拿出50%的库存出来
            stock = stock / 2;
            //按到4:1的比例放在不同的redis中 因为我这里是每个商品库存10个，4:1 比较容易计算
            int redis1Stock = stock / 5 * 4;
            int redis2Stock = stock / 5 * 1;
            //设置超时时间 24小时
            String key = "product:flash_sale:" + product.getId();

            if (Boolean.TRUE.equals(redisTemplate.hasKey(key)) || Boolean.TRUE.equals(redisTemplate2.hasKey(key))) {
                //删除错误信息
                redisTemplate.delete(key);
                redisTemplate2.delete(key);
                break;
            }

            Set<String> keys = redisTemplate.keys("*" + key + "*");
            Set<String> keys2 = redisTemplate2.keys("*" + key + "*");
            // 判断是否存在符合模式的 key
            if ((keys != null && !keys.isEmpty())
                    || (keys2 != null && !keys2.isEmpty())) {
                System.out.println("活动尚未结束");
                mProductInRedis1.addAll(keys);
                mProductInRedis2.addAll(keys2);
                break;
            }

            //先检查缓存中有没有存在的秒杀商品 有就不加载
            for (int i = 0; i < redis1Stock; i++) {
                String itemKey = key + ":" + i;
                product.setStock(1);
                redisTemplate.opsForValue().set(itemKey, product, 24, TimeUnit.HOURS);
                mProductInRedis1.add(itemKey);
            }

            for (int i = 0; i < redis2Stock; i++) {
                String itemKey = key + ":" + i;
                product.setStock(1);
                redisTemplate2.opsForValue().set(itemKey, product, 24, TimeUnit.HOURS);
                mProductInRedis2.add(itemKey);
            }
        }

    }

}
