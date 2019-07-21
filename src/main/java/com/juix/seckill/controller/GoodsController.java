package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.SecKillGoodsEnums;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 20:53
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/list")
    public Result<List<GoodsVo>> list(User user) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        return Result.ok(goodsList);
    }

    @RequestMapping("/detail/{goodsID}")
    public Result<GoodsVo> detail(User user, @PathVariable("goodsID") long goodsID) {
        GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);

        long startAt = good.getStartDate().getTime();
        long endAt = good.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int secKillStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            // 秒杀尚未开始...
            secKillStatus = SecKillGoodsEnums.SECKILL_NOT_START.getCode();
            remainSeconds = (int) (now - startAt);
        } else if (now > endAt) {
            // 秒杀已经结束
            secKillStatus = SecKillGoodsEnums.SECKILL_END.getCode();
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            secKillStatus = SecKillGoodsEnums.SECKILL_LASTING.getCode();
            remainSeconds = 0;
        }
        good.setSecKillStatus(secKillStatus);
        good.setRemainSeconds(remainSeconds);

        return Result.ok(good);
    }
}
