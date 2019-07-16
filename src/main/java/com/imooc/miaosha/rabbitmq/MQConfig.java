package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    //public static final String ROUTING_KEY1 = "topic.key1";
    //public static final String ROUTING_KEY2 = "topic.#";


    @Bean
    public Queue miaoshaQueue(){
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * Direct模式 交换机Exchange
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE, true);
    }
    /**
     * Topic模式 交换机Exchange
     */
    //创建了2个queue，创建了1个交换机，绑定起来
    /**
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1, true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2, true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }
    */











}











