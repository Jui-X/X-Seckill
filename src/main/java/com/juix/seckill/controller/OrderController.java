package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.ServerEnums;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.GoodsVo;
import com.juix.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-22 22:58
 **/
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @RequestMapping("/detail")
    public Result<OrderDetailVo> detail(User user, @RequestParam("orderID")long orderID) {
        if (user == null) {
            return Result.errorException(ServerEnums.SESSION_ERROR.getMsg());
        }

        OrderInfo order = orderService.getOrderByUserID(orderID);

        if (order == null) {
            return Result.errorException(ServerEnums.ORDER_NOT_EXIST.getMsg());
        }

        Long goodsID = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsID(goodsID);

        OrderDetailVo orderDetail = new OrderDetailVo();
        orderDetail.setGoods(goods);
        orderDetail.setOrder(order);

        return Result.ok(orderDetail);
    }
}
