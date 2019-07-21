package com.juix.seckill.utils.key;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-20 21:34
 **/
public class UserKey extends BasePrefix {
    public UserKey( String prefix) {
        super(prefix);
    }

    public static UserKey getByID = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
