package com.juix.seckill.service;

import com.juix.seckill.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoByGoodsID(long goodsID);

    void reduceStock(GoodsVo good);
}
