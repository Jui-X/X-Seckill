package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 19:42
 **/
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/signin")
    public Result<Boolean> signIn(HttpServletResponse response, @Valid UserVo userVo) {
        userService.signIn(response, userVo);
        return Result.ok();
    }
}
