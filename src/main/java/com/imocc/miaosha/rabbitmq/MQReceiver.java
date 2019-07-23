package com.imocc.miaosha.rabbitmq;

import com.imocc.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(Object message){
        String msg = RedisService.beanToString(message);
        log.info(msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
    /**
    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message){
        log.info("topic queue1 message:" + message);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message){
        log.info("topic queue2 message:" + message);
    }*/































}
