package com.fxa.image.client.service;


import com.fxa.image.client.model.LoginResult;
import com.fxa.image.common.RedisConst;
import com.fxa.image.common.config.ExchangeConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.common.message.SortedContent;
import com.fxa.image.common.message.base.CustomMessage;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RedisService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    RabbitTemplate rabbitTemplate;

    public LoginResult randomGetLoginUser (){
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        Set<Object> fields = hashOps.keys(RedisConst.KEY_USER_LOGIN_MAP);
        List<Object> list = fields.stream().toList();
        if (list.isEmpty()) {
            System.out.println("RedisService randomGetLoginUser list empty");
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        String uid = (String) list.get(randomIndex);
        System.out.println("RedisService randomGetLoginUser uid:"+uid);
        Object obj = redisTemplate.opsForHash().get(RedisConst.KEY_USER_LOGIN_MAP,uid);
        if(obj instanceof LoginResult){
            return (LoginResult) obj;
        }
        return null;
    }

    public LoginResult findOrGetLoginInfo(String uid){
        Object obj = redisTemplate.opsForHash().get(RedisConst.KEY_USER_LOGIN_MAP,uid);
        if(obj instanceof LoginResult){
            return (LoginResult) obj;
        }
        return null;
    }

    public void saveLoginInfoToHash(String uid,Object object){
        redisTemplate.opsForHash().put(RedisConst.KEY_USER_LOGIN_MAP,uid,object);
    }

    public List<Object> getRandomHashKeys(String hashKey, int count) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        Set<Object> fields = hashOps.keys(hashKey);

        if (fields.size() < count) {
            count = fields.size();
        }
        //随机取
        List<Object> objects = new ArrayList<>(fields);
        Collections.shuffle(objects);
        List<Object> randomFields = new ArrayList<>();
        for(int i = 0;i < count;i++){
            randomFields.add(objects.get(i));
        }
        return randomFields;
    }

    public void saveContent(Content content) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        //content id 生成
        RedisAtomicLong idCounter = new RedisAtomicLong(RedisConst.KEY_USER_CONTENT_COUNTER, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
       // 使用 incrementAndGet() 方法生成自增长 ID
        long id = idCounter.incrementAndGet();
        content.setId(id);
        content.setCreteTime(System.currentTimeMillis());

        //存储完整内容到内容表
        redisTemplate.opsForHash().put(RedisConst.KEY_CONTENT_HASH,content.getId(),content);

        //一个用户会对应多个内容 这里应该存储一个uid-[content_id,content_id]这样的列表
        //再根据content_id 存储一个有序集合用于计算热度 或者类似于排行榜的其他数据
        redisTemplate.opsForList().leftPush(RedisConst.KEY_USER_CONTENT_LIST+content.getUid(),id);

        SortedContent sortedContent = new SortedContent();
        sortedContent.setId(content.getId());
        sortedContent.setCreteTime(content.getCreteTime());
        sortedContent.setStatus(content.getStatus());
        //存储按热度排序的内容ID到有序集合
        boolean added = Boolean.TRUE.equals(zSetOperations.add(RedisConst.KEY_CONTENT_Z_PREFIX_SCORE, sortedContent.getId(), sortedContent.getScore()));
        System.out.println("saveContent to score:"+added+" id:"+id);
        //存储按时间排序的内容ID到有序集合
        added = Boolean.TRUE.equals(zSetOperations.add(RedisConst.KEY_CONTENT_Z_PREFIX_CREATE_TIME, sortedContent.getId(), sortedContent.getCreteTime()));
        System.out.println("saveContent to crttime:"+added+" id:"+id);
        //存储按审核状态排序的内容ID到有序集合
        String score = sortedContent.getStatus() + "."+ sortedContent.getCreteTime();
        double scoreD = Double.parseDouble(score);
        added = Boolean.TRUE.equals(zSetOperations.add(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, sortedContent.getId(), scoreD));
        System.out.println("saveContent to status:"+added+" id:"+id);
        //这里能不能按照时间和审核状态计算一个得分出来 方便取的时候按照时间降序排列 从新到旧展示

        if(added){
            //这里发送消息到rabbitmq
            CustomMessage<Content> customMessage = new CustomMessage<>(CustomMessage.DataType.CONTENT,CustomMessage.content_publish);
            customMessage.setData(content);
            rabbitTemplate.convertAndSend(ExchangeConst.EXCHANGE_CONTENT_DIRECT,"content",customMessage);
        }

    }
}
