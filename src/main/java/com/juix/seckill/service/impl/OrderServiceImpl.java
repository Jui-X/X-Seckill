package com.juix.seckill.service.impl;

import com.juix.seckill.dao.GoodsDao;
import com.juix.seckill.domain.SecKillGoods;
import com.juix.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 23:47
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public SecKillGoods getSecKillOrderByUserAndGood(Long id, long goodsID) {
        return null;
    }
}
