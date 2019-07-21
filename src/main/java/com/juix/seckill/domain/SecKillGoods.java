package com.juix.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:43
 **/
@Data
public class SecKillGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
