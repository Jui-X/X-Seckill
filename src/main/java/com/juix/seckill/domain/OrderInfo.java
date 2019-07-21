package com.juix.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:44
 **/
@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
