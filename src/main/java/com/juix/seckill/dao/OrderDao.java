package com.juix.seckill.dao;

import com.juix.seckill.domain.OrderInfo;
import com.juix.seckill.domain.SecKillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("SELECT * FROM order WHERE order_id = #{orderID}")
    OrderInfo getOrderByID(@Param("orderID") long orderID);

    @Select("SELECT * FROM secKill_order WHERE user_id = #{userId} AND goods_id = #{goodsId}")
    SecKillOrder getSecKillOrderByUserAndGoods(@Param("userId")long userID, @Param("goodsId")long goodsID);

    @Insert("INSERT INTO order(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) VALUES(" +
            "#{userID}, #{goodsID}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insertOrder(OrderInfo order);

    @Insert("INSERT INTO sec_kill_order(user_id, goods_id, order_id) VALUES (#{userId}, #{goodsId}, #{orderId})")
    void insertSecKillOrder(SecKillOrder secKillOrder);
}
