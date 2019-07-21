package com.juix.seckill.exception;

import com.juix.seckill.enums.Enums;
import lombok.Getter;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 18:59
 **/
@Getter
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = -972110861333619813L;

    private Enums status;

    public GlobalException(Enums status) {
        super(status.getMsg());
        this.status = status;
    }
}
