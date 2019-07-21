package com.juix.seckill.utils.key;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 20:22
 **/
public class TokenKey extends BasePrefix {
    private static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public TokenKey(String prefix, int expireSeconds) {
        super(prefix);
    }

    public static TokenKey getToken = new TokenKey("token", TOKEN_EXPIRE);
}
