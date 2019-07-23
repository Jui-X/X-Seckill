package com.juix.seckill.rabbitmq;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 11:11
 **/
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService secKillService;

    @RabbitListener(queues = MQConfig.SEC_KILL_QUEUE)
    public void receive(String msg) {
        log.info("Receive SecKill Queue message; " + msg);
        SecKillOrderInfo orderInfo = RedisService.stringToPOJO(msg, SecKillOrderInfo.class);
        User user = orderInfo.getUser();
        long goodsID = orderInfo.getGoodsId();

        GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);
        int stock = good.getStockCount();
        if (stock < 0) {
            return;
        }

        SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(user.getId(), goodsID);
        if (secKillOrder != null) {
            return;
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo order = secKillService.secKillGoods(user, good);
    }


    /*@RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("Receive Queue message; " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE)
    public void receiveTopic(String message) {
        log.info("Receive Topic Queue message; " + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeader(byte[] message) {
        log.info("Receive Header Queue message; " + new String(message));
    }*/
}
