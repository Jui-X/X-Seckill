package com.juix.seckill.vo;

import lombok.Data;

import java.util.Date;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:51
 **/
@Data
public class GoodsVo {
    private Double secKillGoods;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private int secKillStatus;
    private int remainSeconds;
}
