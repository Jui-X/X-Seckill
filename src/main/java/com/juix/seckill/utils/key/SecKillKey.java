package com.juix.seckill.utils.key;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 16:51
 **/
public class SecKillKey extends BasePrefix {
    public SecKillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SecKillKey isSecKillEnd = new SecKillKey(0, "secKillEnd");
    public static SecKillKey getSecKillPath = new SecKillKey(30, "secKillPath");
}
