package com.juix.seckill.service;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.vo.GoodsVo;

public interface OrderService {
    SecKillOrder getSecKillOrderByUserAndGood(Long userID, long goodsID);

    OrderInfo createOrder(User user, GoodsVo good);
}

