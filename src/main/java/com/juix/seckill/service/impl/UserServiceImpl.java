package com.juix.seckill.service.impl;

import com.juix.seckill.dao.UserDao;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.ServerEnums;
import com.juix.seckill.exception.GlobalException;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.MD5Util;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.UUIDUtil;
import com.juix.seckill.utils.key.TokenKey;
import com.juix.seckill.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 11:10
 **/
@Service
public class UserServiceImpl implements UserService {

    public static final String COOKIE_NAME = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    @Override
    public User getUserByID(long id) {
        // 首先从缓存中取出
        User user = redisService.get(TokenKey.getUserByID, "" + id, User.class);

        if (user != null) {
            return user;
        }

        // 没有则从数据库取出，然后存入Redis缓存中
        user = userDao.getUserByUserID(id);
        if (user != null) {
            redisService.set(TokenKey.getUserByID, "" + id, user);
        }

        return user;
    }

    @Override
    public boolean updatePassword(long id, String newPassword, String token) {
        // 从Redis缓存中取出
        User user = getUserByID(id);
        if (user == null) {
            throw new GlobalException(ServerEnums.PHONE_NUMBER_NOT_EXIST);
        }
        // 更新数据库
        // 新建对象以保证修改哪个字段就更新哪个字段，优化性能
        User userToUpdate = new User();
        userToUpdate.setId(id);
        userToUpdate.setPassword(MD5Util.formPassToDBPass(newPassword, user.getSalt()));
        userDao.updatePassword(userToUpdate);
        // 处理缓存
        user.setPassword(userToUpdate.getPassword());
        redisService.delete(TokenKey.getUserByID, "" + id);
        redisService.set(TokenKey.getToken, "" + token, user);

        return true;
    }

    @Override
    public boolean signIn(HttpServletResponse response, UserVo userVo) {
        if (userVo == null) {
            throw new GlobalException(ServerEnums.SERVER_ERROR);
        }

        String phoneNum = userVo.getPhoneNumber();
        String formPass = userVo.getPassword();

        User currentUser = getUserByPhoneNumber(phoneNum);
        if (currentUser == null) {
            throw new GlobalException(ServerEnums.PHONE_NUMBER_NOT_EXIST);
        }
        String dbPass = currentUser.getPassword();
        String salt = currentUser.getSalt();
        String password = MD5Util.formPassToDBPass(formPass, salt);
        if (!password.equals(dbPass)) {
            throw new GlobalException(ServerEnums.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        generateCookie(response, token, currentUser);

        return true;
    }

    public User getUserByPhoneNumber(String phoneNum) {
        return userDao.getUserByPhoneNumber(Long.valueOf(phoneNum));
    }

    public void generateCookie(HttpServletResponse response, String token, User user) {
        redisService.set(TokenKey.getToken, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(TokenKey.getToken.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getUserByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(TokenKey.getToken, token, User.class);

        if (user != null) {
            generateCookie(response, token, user);
        }

        return user;
    }
}
