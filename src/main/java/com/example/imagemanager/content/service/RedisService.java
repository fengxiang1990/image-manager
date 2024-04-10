package com.example.imagemanager.content.service;


import com.fxa.image.common.RedisConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.common.message.SortedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class RedisService {

    private final Logger logger = LoggerFactory.getLogger("RedisService");

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 获取需要人工审核的内容列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> getNoneReviewContents(int page, int pageSize) {
        Map<String, Object> res = new HashMap<>();
        List<Content> list = new ArrayList<>();
        //考虑时间戳2+时间戳 2.XXXXXXX 一定小于3 因此这里取到的范围是0.xxxxx-2.xxxxx
        Long count = redisTemplate.opsForZSet().count(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, 0, 3);
        //0 默认待审核状态 1自动审核通过 2 自动审核不通过 3 人工审核通过 4 人工审核不通过
        int offset = (page - 1) * pageSize;
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        //倒序排列  比如两个状态一样的 都是2 一个先一个后 2.1 2.2 倒序后就是2.2 2.1 这样时间新的排在前面
        Set<Object> objectSet = zSetOperations.reverseRangeByScore(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, 0, 3, offset, pageSize);
        if (objectSet != null) {
            logger.debug("getNoneReviewContents:" + objectSet.size());
            List<Object> contents = redisTemplate.opsForHash().multiGet(RedisConst.KEY_CONTENT_HASH, objectSet);
            contents.forEach(object -> {
                if (object instanceof Content) {
                    list.add((Content) object);
                }
            });
        }
        //对list 按照时间排序
        //只能等对当前页的数据排序
//        list.sort(new Comparator<Content>() {
//            @Override
//            public int compare(Content o1, Content o2) {
//                if(o1.getCreteTime() < o2.getCreteTime()){
//                    return 1;
//                }else if(o1.getCreteTime() > o2.getCreteTime()){
//                    return -1;
//                }
//                return 0;
//            }
//        });

        res.put("data", list);
        res.put("total", count);
        return res;
    }


    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean review(long[] ids) {
        for (long id : ids) {
            //首先获取指定元素的索引
            Long index = redisTemplate.opsForZSet().rank(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id);
            if (index != null && index > -1) {
                //根据索引删除
                Long del = redisTemplate.opsForZSet().remove(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id);
                if (del != null && del == 1) {
                    //重新添加到status有序表中
                    Content content = (Content) redisTemplate.opsForHash().get(RedisConst.KEY_CONTENT_HASH, id);
                    String score = 3 + "." + content.getCreteTime();
                    double scoreD = Double.parseDouble(score);
                    redisTemplate.opsForZSet().add(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id, scoreD);
                    del = redisTemplate.opsForHash().delete(RedisConst.KEY_CONTENT_HASH, id);
                    if (del == 1) {
                        content.setStatus(3);
                        redisTemplate.opsForHash().put(RedisConst.KEY_CONTENT_HASH, id, content);
                    }
                }
            }
        }
        return true;
    }

    public boolean reject(long[] ids) {
        for (long id : ids) {
            //首先获取指定元素的索引
            Long index = redisTemplate.opsForZSet().rank(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id);
            if (index != null && index > -1) {
                //根据索引删除
                Long del = redisTemplate.opsForZSet().remove(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id);
                if (del != null && del == 1) {
                    //重新添加到status有序表中
                    Content content = (Content) redisTemplate.opsForHash().get(RedisConst.KEY_CONTENT_HASH, id);
                    String score = 4 + "." + content.getCreteTime();
                    double scoreD = Double.parseDouble(score);
                    redisTemplate.opsForZSet().add(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS, id, scoreD);
                    del = redisTemplate.opsForHash().delete(RedisConst.KEY_CONTENT_HASH, id);
                    if (del == 1) {
                        content.setStatus(4);
                        redisTemplate.opsForHash().put(RedisConst.KEY_CONTENT_HASH, id, content);
                    }
                }
            }
        }
        return true;
    }
}
