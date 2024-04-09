package com.fxa.image.client.service;

import com.fxa.image.client.proto.ContentOuter;
import com.fxa.image.common.config.ExchangeConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.common.message.base.CustomMessage;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ContentService {
    private Logger LOGGER =  LoggerFactory.getLogger(ContentService.class);

    @Resource
    RedisService redisService;


    public void saveContent(ContentOuter.Content content){
        LOGGER.debug("saveContent content:"+content);
        Content content1 = new Content();
        content1.setUid(content.getAuthor());
        content1.setTitle(content.getTitle());
        content1.setContent(content.getContent());
        content1.setUrl(content.getImageUrl());
        content1.setViews(content.getViews());
        content1.setLike(content.getLikeCnt());
        content1.setLabel(new ArrayList<>(content.getLabelList()));
        content1.setTopic(new ArrayList<>(content.getTopicList()));
        redisService.saveContent(content1);
    }

}
