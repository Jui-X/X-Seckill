package com.juix.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 11:12
 **/
@Configuration
public class MQConfig {

    public static final String SEC_KILL_QUEUE = "sceKill.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE = "topic.queue";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topic.topicExchange";
    public static final String FANOUT_EXCHANGE = "fanout.topicExchange";
    public static final String HEADERS_EXCHANGE = "header.topicExchange";

    /**
     * Direct 直连模式 交换机Exchange
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    /**
     * Topic模式 交换机Exchange
     * 话题模式
     */
    @Bean
    public Queue topicQueue() {
        return new Queue(TOPIC_QUEUE, true);
    }
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("topic.#");
    }

    /**
     * Fanout模式 交换机Exchange
     * 广播模式
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(topicQueue()).to(fanoutExchange());
    }

    /**
     * Headers模式
     */
    @Bean
    public Queue headerQueue() {
        return new Queue(HEADER_QUEUE, true);
    }
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Binding headersBinding() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("header", "value");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }
}
