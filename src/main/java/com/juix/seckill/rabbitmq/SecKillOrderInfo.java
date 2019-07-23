package com.juix.seckill.rabbitmq;

import com.juix.seckill.domain.User;
import lombok.Data;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-23 16:15
 **/
@Data
public class SecKillOrderInfo {
    private User user;
    private long goodsId;
}
