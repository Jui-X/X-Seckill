package com.juix.seckill.dao;

import com.juix.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("SELECT g.*, s.stock_count, s.start_date, s.end_date, s.sec_kill_price FROM secKill_goods s LEFT JOIN goods g on s.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("SELECT g.*, s.stock_count, s.start_date, s.end_date, s.sec_kill_price FROM secKill_goods s LEFT JOIN goods g on s.goods_id = g.id WHERE g.id = #{goodsID}")
    GoodsVo getGoodsVoByGoodsID(@Param("goodsID") long goodsID);

    @Update("UPDATE secKill_goods SET stock_count = stock_count - 1 WHERE goods_id = #{goodsID}")
    void reduceStock(long goodsID);
}
