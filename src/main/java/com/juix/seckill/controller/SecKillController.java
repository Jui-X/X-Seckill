package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 23:40
 **/
@RequestMapping("/seckill")
@RestController
public class SecKillController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SecKillService secKillService;

    @PostMapping("/secKill")
    public Result<OrderInfo> secKill(User user, @RequestParam("goodsID")long goodsID){
        if (user == null) {
            return Result.errorException("用户尚未登录！请先登录或注册～");
        }

        GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);
        int stock = good.getStockCount();
        if (stock < 0) {
            return Result.errorException("当前秒杀商品库存不足～");
        }

        SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(user.getId(), goodsID);
        if (secKillOrder != null) {
            return Result.errorException("已抢购过此商品，不能再次抢购~");
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = secKillService.secKillGoods(user, good);

        return Result.ok(orderInfo);
    }

}
