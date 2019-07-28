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
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-28 20:21
 **/
public class MQProducer {

    private DefaultMQProducer mqProducer;

    private TransactionMQProducer transactionMQProducer;

    @Value("127.0.0.1:9876")
    private String nameAddr;

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
        mqProducer = new DefaultMQProducer();
        mqProducer.setNamesrvAddr(nameAddr);
        mqProducer.start();

        transactionMQProducer = new TransactionMQProducer();
        transactionMQProducer.setNamesrvAddr(nameAddr);
        transactionMQProducer.start();

        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                SecKillOrderInfo orderInfo = (SecKillOrderInfo) o;

                User user = orderInfo.getUser();
                long goodsID = orderInfo.getGoodsId();

                GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);
                int stock = good.getStockCount();
                if (stock < 0) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                // 确认数据库中是否有唯一订单
                // 也可以通过唯一索引来实现
                SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(user.getId(), goodsID);
                if (secKillOrder != null) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                // 减库存 下订单 写入秒杀订单
                OrderInfo order = secKillService.secKillGoods(user, good);

                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                SecKillOrderInfo orderInfo = RedisService.stringToPOJO(new String(messageExt.getBody()), SecKillOrderInfo.class);

                Long userID = orderInfo.getUser().getId();
                Long goodsID = orderInfo.getGoodsId();

                if (orderService.getSecKillOrderByUserAndGood(userID, goodsID) == null) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            }
        });
    }

    public boolean transactionAsyncReduceStock(SecKillOrder orderInfo) {
        String msg = RedisService.POJOToString(orderInfo);
        Message message = new Message(topic, "decrease", msg.getBytes());
        TransactionSendResult sendResult = null;

        try {
            sendResult = transactionMQProducer.sendMessageInTransaction(message, orderInfo);
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        if (sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
            return false;
        } else if (sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean asyncReduceStock(SecKillOrder orderInfo) {
        String msg = RedisService.POJOToString(orderInfo);
        Message message = new Message(topic, "decrease", msg.getBytes());

        try {
            mqProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
