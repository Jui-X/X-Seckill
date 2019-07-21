package com.juix.seckill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 11:04
 **/
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isPhoneNumber(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum)) {
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(phoneNum);
        return matcher.matches();
    }
}
