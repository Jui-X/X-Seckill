package com.juix.seckill.rabbitmq;

import com.juix.seckill.utils.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void sendSecKillOrderInfo(SecKillOrderInfo orderInfo) {
        String msg = RedisService.POJOToString(orderInfo);
        log.info("Send SecKill Queue msg: " + msg);
        amqpTemplate.convertAndSend(MQConfig.SEC_KILL_QUEUE, msg);
    }


    /*public <T> void sender(T message) {
        String msg = RedisService.POJOToString(message);
        log.info("Send Queue msg: " + msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }

    public <T> void sendTopic(T message) {
        String msg = RedisService.POJOToString(message);
        log.info("Send Topic Queue msg: " + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key", msg);
    }

    public <T> void sendFanout(T message) {
        String msg = RedisService.POJOToString(message);
        log.info("Send Fanout Queue msg: " + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
    }

    public <T> void sendHeader(T message) {
        String msg = RedisService.POJOToString(message);
        log.info("Send Header Queue msg: " + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header", "value");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", msg);
    }*/
}
