package com.juix.seckill.service.impl;

import com.juix.seckill.dao.GoodsDao;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:45
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsID(long goodsID) {
        return goodsDao.getGoodsVoByGoodsID(goodsID);
    }
}
