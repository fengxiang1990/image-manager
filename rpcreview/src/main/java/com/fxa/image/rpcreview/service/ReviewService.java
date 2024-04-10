package com.fxa.image.rpcreview.service;

import com.fxa.image.common.RedisConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.rpcreview.model.ReviewContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReviewService");


    private final RedisTemplate<String,Object> redisTemplate;

    public ReviewService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void contentAutoReview(ReviewContent content){
       Object object =  redisTemplate.opsForHash().get(RedisConst.KEY_CONTENT_HASH,content.getId());
       if(object == null){
           LOGGER.debug("content:"+content.getId()+" got from redis is null");
           return;
       }
       if(object instanceof Content contentRedis){
           contentRedis.setStatus(1);
           redisTemplate.opsForHash().put(RedisConst.KEY_CONTENT_HASH,content.getId(),contentRedis);

           String score = contentRedis.getStatus() + "."+ contentRedis.getCreteTime();
           double scoreD = Double.parseDouble(score);
           long id = contentRedis.getId();
           //更新状态表
           Long index = redisTemplate.opsForZSet().rank(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS,id);
           if(index!=null && index > -1){
               //根据索引删除
               Long del =  redisTemplate.opsForZSet().remove(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS,id);
               if(del != null && del == 1){
                   //重新添加到status有序表中
                   redisTemplate.opsForZSet().add(RedisConst.KEY_CONTENT_Z_PREFIX_STATUS,id,scoreD);
               }
           }

           LOGGER.debug("auto review content:"+content.getId()+" success");
       }
    }

    public void commentAutoReview(){

    }

}
