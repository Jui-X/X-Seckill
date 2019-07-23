package com.juix.seckill.config;

import com.juix.seckill.access.UserContext;
import com.juix.seckill.domain.User;
import com.juix.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @param: none
 * @description: 用于预处理传入参数
 * @author: KingJ
 * @create: 2019-07-21 21:38
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        return UserContext.getUser();
    }


}
