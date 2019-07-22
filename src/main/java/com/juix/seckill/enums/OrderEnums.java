package com.juix.seckill.enums;

import lombok.Getter;

@Getter
public enum OrderEnums {
    NOT_PAID(0, "新建未支付"),
    PAID(1, "已支付"),
    DELIVERY(2, "已发货"),
    RECEIVED(3, "已收货"),
    REFUND(4, "已退款"),
    FINISHED(5, "已完成")
    ;
    private int code;
    private String status;

    OrderEnums(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
