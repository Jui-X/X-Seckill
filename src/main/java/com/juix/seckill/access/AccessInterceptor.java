package com.juix.seckill.access;

import com.alibaba.fastjson.JSON;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.ServerEnums;
import com.juix.seckill.service.UserService;
import com.juix.seckill.service.impl.UserServiceImpl;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.key.AccessKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 21:13
 **/
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = getUser(request, response);
        UserContext.setUser(user);

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, ServerEnums.SESSION_ERROR.getMsg());
                    return false;
                }
                key += "_" + user.getId();
            }

            Integer count = redisService.get(AccessKey.withExpire(seconds), key, Integer.class);
            if (count == null) {
                redisService.set(AccessKey.withExpire(seconds), key, 1);
            } else if (count < maxCount) {
                redisService.incr(AccessKey.withExpire(seconds), key);
            } else {
                render(response, ServerEnums.ACCESS_LIMITED.getMsg());
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    private void render(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        String str = JSON.toJSONString(msg);
        outputStream.write(msg.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserServiceImpl.COOKIE_NAME);
        String cookieToken = getCookieValue(request, UserServiceImpl.COOKIE_NAME);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getUserByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length <= 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
