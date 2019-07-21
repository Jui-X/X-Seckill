package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 20:53
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/list")
    public Result<Boolean> list() {
        return Result.ok();
    }
}
