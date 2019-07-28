package com.juix.seckill.service.impl;

import com.juix.seckill.dao.OrderDao;
import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.OrderEnums;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.key.OrderKey;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 23:47
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    @Override
    public OrderInfo getOrderByUserID(long orderID) {

        return orderDao.getOrderByID(orderID);
    }

    @Override
    public SecKillOrder getSecKillOrderByUserAndGood(Long userID, long goodsID) {
        SecKillOrder secKillOrder = redisService.get(OrderKey.getSecKillOrderByUserAndGood, "" + userID + "_" + goodsID, SecKillOrder.class);
        if (secKillOrder != null) {
            return secKillOrder;
        }

        return orderDao.getSecKillOrderByUserAndGoods(userID, goodsID);
    }

    @Override
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo good) {
        OrderInfo order = new OrderInfo();
        order.setUserId(user.getId());
        order.setGoodsId(good.getId());
        order.setGoodsPrice(good.getSecKillGoodsPrice());
        order.setGoodsName(good.getGoodsName());
        order.setGoodsCount(1);
        order.setDeliveryAddrId(0L);
        order.setStatus(OrderEnums.NOT_PAID.getCode());
        order.setOrderChannel(1);
        order.setCreateDate(new Date());

        orderDao.insertOrder(order);

        SecKillOrder secKillOrder = new SecKillOrder();
        secKillOrder.setUserId(user.getId());
        secKillOrder.setGoodsId(good.getId());
        secKillOrder.setOrderId(order.getId());

        orderDao.insertSecKillOrder(secKillOrder);

        // 写入Redis缓存中
        // 秒杀订单落库后写入Redis，通过缓存方式缓解数据库压力
        redisService.set(OrderKey.getSecKillOrderByUserAndGood, "" + user.getId() + "_" + good.getId(), secKillOrder);

        return order;
    }
}
