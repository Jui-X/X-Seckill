package com.juix.seckill.enums;

import lombok.Getter;

@Getter
public enum SecKillGoodsEnums {
    SECKILL_NOT_START(0),
    SECKILL_LASTING(1),
    SECKILL_END(2)
    ;

    private int code;

    SecKillGoodsEnums(int code) {
        this.code = code;
    }
}
