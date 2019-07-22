package com.juix.seckill.dao;

import com.juix.seckill.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserByUserID(@Param("id") long id);

    @Select("SELECT * FROM user WHERE phone_num = #{phone_num}")
    User getUserByPhoneNumber(@Param("phone_num") Long phoneNumber);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    void updatePassword(User userToUpdate);
}
