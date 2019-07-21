package com.juix.seckill.enums;

import lombok.Getter;

@Getter
public enum Enums {
    SUCCESS(200, "登陆成功"),
    SERVER_ERROR(500, "服务器内部异常"),
    SERVER_NOT_IMPLEMENTED(501, "不支持的请求"),
    SERVER_UNAVAILABLE(503, "服务不可用"),
    SERVER_EXCEPTION(555, "服务器内部自定义异常"),
    SERVER_PARAMETER_ERROR(601, "参数校验异常: %s"),
    PHONE_NUMBER_NOT_EXIST(602, "当前手机号为注册！"),
    PASSWORD_ERROR(603, "当前账号或密码错误！")
    ;

    private int code;
    private String msg;

    Enums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String fillParameter(Object... parameters) {
        String msg = String.format(SERVER_PARAMETER_ERROR.getMsg(), parameters);
        return msg;
    }
}
