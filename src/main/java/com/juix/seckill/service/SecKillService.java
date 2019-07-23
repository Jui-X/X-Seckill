package com.juix.seckill.service;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.User;
import com.juix.seckill.vo.GoodsVo;

public interface SecKillService {

    OrderInfo secKillGoods(User user, GoodsVo good);

    long secKillResult(long userID, long goodsID);

    String createSecKillPath(User user, long goodsID);

    boolean checkPath(User user, long goodsID, String path);
}
