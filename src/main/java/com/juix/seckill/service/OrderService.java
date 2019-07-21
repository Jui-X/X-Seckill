package com.juix.seckill.service;

import com.juix.seckill.domain.SecKillGoods;

public interface OrderService {
    SecKillGoods getSecKillOrderByUserAndGood(Long id, long goodsID);
}

