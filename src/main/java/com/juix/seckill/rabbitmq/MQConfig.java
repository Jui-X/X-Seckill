package com.juix.seckill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 11:12
 **/
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
}
