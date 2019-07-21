package com.juix.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 09:57
 **/
public class MD5Util {

    private static final String salt = "9a8b7c6d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(7) + inputPass + salt.charAt(5) + salt.charAt(2);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(1) + salt.charAt(3);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass) {
        String salt = String.valueOf(System.currentTimeMillis());
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }
}
