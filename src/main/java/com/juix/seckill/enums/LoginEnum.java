package com.juix.seckill.enums;

public enum LoginEnum {
    SUCCESS(200, "登陆成功"),
    SERVER_ERR(500, "服务器内部异常"),
    SERVER_UNAVAILABLE(503, "服务不可用"),
    ;

    private int code;
    private String msg;

    LoginEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
