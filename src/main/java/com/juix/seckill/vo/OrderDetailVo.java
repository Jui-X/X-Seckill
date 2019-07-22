package com.juix.seckill.vo;

import com.juix.seckill.domain.OrderInfo;
import lombok.Data;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-22 22:58
 **/
@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
