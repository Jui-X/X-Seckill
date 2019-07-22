package com.juix.seckill.service.impl;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.User;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 23:51
 **/
@Service
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * 减少库存
     * 下订单
     * 写入秒杀订单
     * @param user
     * @Param good
     * @return
     */
    @Override
    @Transactional
    public OrderInfo secKillGoods(User user, GoodsVo good) {
        goodsService.reduceStock(good);

        return orderService.createOrder(user, good);
    }
}
