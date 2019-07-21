package com.juix.seckill.domain;

import lombok.Data;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 22:44
 **/
@Data
public class SecKillOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
