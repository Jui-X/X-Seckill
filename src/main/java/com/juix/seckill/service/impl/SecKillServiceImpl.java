package com.juix.seckill.service.impl;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import com.juix.seckill.domain.User;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.OrderService;
import com.juix.seckill.service.SecKillService;
import com.juix.seckill.utils.MD5Util;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.key.SecKillKey;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Autowired
    RedisService redisService;

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
        if (!goodsService.reduceStock(good)) {
            setSecKillEnd(good.getId());
            return null;
        } else {
            return orderService.createOrder(user, good);
        }
    }

    @Override
    public long secKillResult(long userID, long goodsID) {
        SecKillOrder secKillOrder = orderService.getSecKillOrderByUserAndGood(userID, goodsID);
        if (secKillOrder != null) {
            return secKillOrder.getOrderId();
        } else {
            boolean isEnd = getSecKillEnd(goodsID);
            if (isEnd) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String createSecKillPath(User user, long goodsID) {
        if (user == null || goodsID <= 0) {
            return null;
        }

        String str = MD5Util.md5(UUID.randomUUID() + "1a2b3c");
        redisService.set(SecKillKey.getSecKillPath, "" + user.getId() + "_" + goodsID, str);

        return str;
    }

    @Override
    public boolean checkPath(User user, long goodsID, String path) {
        if (user == null || path == null) {
            return false;
        }

        String realPath = redisService.get(SecKillKey.getSecKillPath, "" + user.getId() + "_" + goodsID, String.class);
        return path.equals(realPath);
    }

    private void setSecKillEnd(Long goodsID) {
        redisService.set(SecKillKey.isSecKillEnd, "" + goodsID, true);
    }

    private boolean getSecKillEnd(long goodsID) {
        return redisService.exist(SecKillKey.isSecKillEnd, "" + goodsID);
    }
}
