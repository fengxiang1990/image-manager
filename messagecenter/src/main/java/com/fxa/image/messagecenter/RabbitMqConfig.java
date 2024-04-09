package com.fxa.image.messagecenter;

import com.fxa.image.common.config.ExchangeConst;
import com.fxa.image.common.message.TestMessage;
import com.fxa.image.common.message.base.CustomMessage;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig implements InitializingBean {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;



    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAutoStartup(true); // 自动启动容器
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    public void createExchange(String exchangeName, String exchangeType) {
        Exchange exchange;
        if ("direct".equalsIgnoreCase(exchangeType)) {
            exchange = new DirectExchange(exchangeName);
        } else if ("fanout".equalsIgnoreCase(exchangeType)) {
            exchange = new FanoutExchange(exchangeName);
        } else if ("topic".equalsIgnoreCase(exchangeType)) {
            exchange = new TopicExchange(exchangeName);
        } else if ("headers".equalsIgnoreCase(exchangeType)) {
            exchange = new HeadersExchange(exchangeName);
        } else {
            throw new IllegalArgumentException("Invalid exchange type: " + exchangeType);
        }
        amqpAdmin.declareExchange(exchange);
    }

    public void createQueue(String queueName) {
        Queue queue = new Queue(queueName,true);
        amqpAdmin.declareQueue(queue);
    }

    public void bindQueueToExchange(String queueName, String exchangeName, String routingKey) {
        Binding binding = BindingBuilder.bind(new Queue(queueName))
                .to(new DirectExchange(exchangeName))
                .with(routingKey);
        amqpAdmin.declareBinding(binding);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        createExchange(ExchangeConst.EXCHANGE_CONTENT_DIRECT,"direct");
        createQueue(ExchangeConst.QUEUE_CONTENT_PUBLISH);
        bindQueueToExchange(ExchangeConst.QUEUE_CONTENT_PUBLISH,ExchangeConst.EXCHANGE_CONTENT_DIRECT,"content");

//        CustomMessage<TestMessage> customMessage = new CustomMessage<>(CustomMessage.DataType.TESTMSG,CustomMessage.content_publish);
//        customMessage.setData(new TestMessage("222"));
//        rabbitTemplate.convertAndSend(ExchangeConst.EXCHANGE_CONTENT_DIRECT,"content",customMessage);

    }



}