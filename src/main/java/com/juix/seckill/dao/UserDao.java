package com.juix.seckill.dao;

import com.juix.seckill.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE phone_num = #{phone_num}")
    User getUserByPhoneNumber(@Param("phone_num") Long phoneNumber);
}
