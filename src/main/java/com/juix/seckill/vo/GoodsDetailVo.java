package com.juix.seckill.vo;

import com.juix.seckill.domain.User;
import lombok.Data;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-22 22:10
 **/
@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private User user;
}
