package com.juix.seckill.service;

import com.juix.seckill.domain.User;
import com.juix.seckill.vo.UserVo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface UserService {

    User getUserByID(long id);

    boolean updatePassword(long id, String newPassword, String token);

    boolean signIn(HttpServletResponse response, @Valid UserVo userVo);

    User getUserByToken(HttpServletResponse response, String token);
}
