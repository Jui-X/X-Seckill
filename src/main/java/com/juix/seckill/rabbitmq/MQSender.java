package com.juix.seckill.rabbitmq;

import com.juix.seckill.utils.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.juix.seckill.rabbitmq.MQConfig.QUEUE;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 11:11
 **/
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public <T> void sender(T message) {
        String msg = RedisService.POJOToString(message);
        log.info("Send Queue msg: " + msg);
        amqpTemplate.convertAndSend(QUEUE, msg);
    }
}
