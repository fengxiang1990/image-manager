package com.fxa.image.messagecenter;


import com.fxa.image.common.config.ExchangeConst;
import com.fxa.image.common.message.Content;
import com.fxa.image.common.message.TestMessage;
import com.fxa.image.common.message.base.CustomMessage;
import com.fxa.image.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageListener {

    //    @RabbitListener(queues = ExchangeConst.QUEUE_CONTENT_PUBLISH)
//    public void handleMessage(String message) {
//        // 处理接收到的消息
//        System.out.println("Received message: " + message);
//    }
    @Resource
    private RestTemplate restTemplate;


    @RabbitListener(queues = ExchangeConst.QUEUE_CONTENT_PUBLISH)
    public void handleMessage(CustomMessage<?> message) {
        // 处理接收到的消息
        Object data = message.getData();

        switch (message.getDataType()) {
            case CustomMessage.DataType.CONTENT -> {
                try {
                    Content content = (Content) data;
                    System.out.println("recv content msg:" + content.toString());
                    //todo 调用 审核微服务 这里是自动审核
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    // 设置请求体
                    String requestBody = JsonUtils.toJson(content);
                    // 创建 HttpEntity
                    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                    String res = restTemplate.postForObject("http://rpcreview/autoreview/content", requestEntity, String.class);
                    System.out.println("res:" + res);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            case CustomMessage.DataType.TESTMSG -> {
                TestMessage testMessage = (TestMessage) data;
                System.out.println("recv test msg");
            }
        }
    }

}
