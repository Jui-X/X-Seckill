package com.juix.seckill.access;

import com.juix.seckill.domain.User;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 21:22
 **/
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
