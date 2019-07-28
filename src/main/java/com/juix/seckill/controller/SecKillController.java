package com.juix.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.juix.seckill.access.AccessLimit;
import com.juix.seckill.common.Result;
import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.ServerEnums;
import com.juix.seckill.rabbitmq.MQSender;
import com.juix.seckill.rabbitmq.SecKillOrderInfo;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.key.GoodsKey;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 23:40
 **/
@RequestMapping("/seckill")
@RestController
public class SecKillController implements InitializingBean {

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

    @Autowired
    MQSender mqSender;

    private RateLimiter secKillRateLimiter;

    private ConcurrentHashMap<Long, Boolean> secKillEndMap = new ConcurrentHashMap<>();

    /**
     * 继承InitializingBean接口
     * 实现系统初始化时加载数据库数据到Redis缓存中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSecKillGoodStock, "" + goods.getId(), goods.getStockCount());
            secKillEndMap.put(goods.getId(), false);
        }

        secKillRateLimiter = RateLimiter.create(300);
    }

    @PostMapping("/{path}/secKill")
    public Result<OrderInfo> secKill(User user, @RequestParam("goodsID") long goodsID,
                                     @PathVariable("path")String path) {
        if (user == null) {
            return Result.errorException(ServerEnums.SESSION_ERROR.getMsg());
        }

        // 验证接口url
        if (!secKillService.checkPath(user, goodsID, path)) {
            return Result.errorException(ServerEnums.REQUEST_ILLEGAL.getMsg());
        }

        // 内存标记
        // 用HashMap以商品ID为键，商品库存是否小于0为值，以内存对象为缓存减少缓存数据库压力
        Boolean end = secKillEndMap.get(goodsID);
        if (end) {
            return Result.errorException(ServerEnums.SEC_KILL_END.getMsg());
        }

        // Redis缓存中预减库存
        Long stock = redisService.decr(GoodsKey.getSecKillGoodStock, "" + goodsID);
        if (stock < 0) {
            secKillEndMap.put(goodsID, true);
            return Result.errorException(ServerEnums.SEC_KILL_END.getMsg());
        }

        // 查询秒杀订单 判断是否已经秒杀到商品
        SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(user.getId(), goodsID);
        if (secKillOrder != null) {
            return Result.errorException(ServerEnums.SEC_KILL_AGAIN.getMsg());
        }

        SecKillOrderInfo orderInfo = new SecKillOrderInfo();
        orderInfo.setGoodsId(goodsID);
        orderInfo.setUser(user);
        // 入队
        mqSender.sendSecKillOrderInfo(orderInfo);

        return Result.ok(ServerEnums.HANDLING.getMsg());

        /*GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);
        int stock = good.getStockCount();
        if (stock < 0) {
            return Result.errorException("当前秒杀商品库存不足～");
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = secKillService.secKillGoods(user, good);

        return Result.ok(orderInfo);*/
    }

    /**
     * 成功返回orderID
     * 处理中返回 0
     * 失败返回 -1
     *
     * @param user
     * @param goodsID
     * @param <T>
     * @return
     */
    @GetMapping("/secKillResult")
    public <T> Result<T> secKillResult(User user, @RequestParam("goodsID") long goodsID) {
        if (user == null) {
            return Result.errorException(ServerEnums.SESSION_ERROR.getMsg());
        }

        long result = secKillService.secKillResult(user.getId(), goodsID);

        return Result.ok(result);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/secKillResult")
    public Result<String> getSecKillPath(HttpServletRequest request, User user, @RequestParam("goodsID") long goodsID) {
        if (secKillRateLimiter.acquire() <= 0) {
            return Result.errorException(ServerEnums.SEC_KILL_RATE_LIMIT.getMsg());
        }

        if (user == null) {
            return Result.errorException(ServerEnums.SESSION_ERROR.getMsg());
        }

        String path = secKillService.createSecKillPath(user, goodsID);

        return Result.ok(path);
    }


}
