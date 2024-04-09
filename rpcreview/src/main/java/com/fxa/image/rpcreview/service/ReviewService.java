package com.fxa.image.rpcreview.service;

import com.fxa.image.common.RedisConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.rpcreview.model.ReviewContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReviewService");


    private final RedisTemplate<String,Object> redisTemplate;

    public ReviewService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void contentAutoReview(ReviewContent content){
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        // 查找记录并修改 status
        Set<Object> contents = zSetOperations.rangeByScore(RedisConst.KEY_USER_CONTENT_Z_PREFIX + content.getId(), content.getScore(), content.getScore());
        if(contents == null){
            LOGGER.error("contentAutoReview failed contents null");
            return;
        }
        for (Object obj : contents) {
            // 假设 Content 对象具有 getStatus() 和 setStatus() 方法
            Content redisContent = (Content) obj;
            if (redisContent.getId() == content.getId()) {
                //假设这里有一个自动内容审核工具且审核通过
                //默认待审核状态 1自动审核通过 2 自动审核不通过 3 人工审核通过 4 人工审核不通过
                redisContent.setStatus(1);
                // 使用 add() 方法更新记录
                boolean added = Boolean.TRUE.equals(zSetOperations.add(RedisConst.KEY_USER_CONTENT_Z_PREFIX + content.getUid(), redisContent, redisContent.getScore()));
                LOGGER.debug("contentAutoReview suc:"+added);
                break; // 假设只修改一条记录，可以直接退出循环
            }
        }
    }

    public void commentAutoReview(){

    }

}
