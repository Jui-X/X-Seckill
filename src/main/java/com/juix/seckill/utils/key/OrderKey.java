package com.juix.seckill.utils.key;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-20 21:34
 **/
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSecKillOrderByUserAndGood = new OrderKey("secKill");
}
