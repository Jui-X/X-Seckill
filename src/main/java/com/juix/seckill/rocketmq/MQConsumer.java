package com.juix.seckill.rocketmq;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.rabbitmq.SecKillOrderInfo;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.GoodsVo;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-28 20:21
 **/
public class MQConsumer {

    private DefaultMQPushConsumer consumer;

    @Value("127.0.0.1:9876")
    private String addrName;

    @Value("secKill")
    private String topic;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService secKillService;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(addrName);
        consumer.subscribe(topic, "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            // 实现真正扣减库存逻辑
            Message message = list.get(0);
            SecKillOrderInfo orderInfo = RedisService.stringToPOJO(new String(list.get(0).getBody()), SecKillOrderInfo.class);

            User user = orderInfo.getUser();
            long goodsID = orderInfo.getGoodsId();

            GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);
            int stock = good.getStockCount();
            if (stock < 0) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            // 确认数据库中是否有唯一订单
            // 也可以通过唯一索引来实现
            SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(user.getId(), goodsID);
            if (secKillOrder != null) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            // 减库存 下订单 写入秒杀订单
            OrderInfo order = secKillService.secKillGoods(user, good);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }

}
