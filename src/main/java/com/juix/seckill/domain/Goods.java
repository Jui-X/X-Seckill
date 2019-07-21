package com.juix.seckill.domain;

import lombok.Data;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:43
 **/
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
