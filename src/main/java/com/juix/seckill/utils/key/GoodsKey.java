package com.juix.seckill.utils.key;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-22 20:24
 **/
public class GoodsKey extends BasePrefix {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodList");
    public static GoodsKey getSecKillGoodStock = new GoodsKey(0, "secKillGoodStock");
}
