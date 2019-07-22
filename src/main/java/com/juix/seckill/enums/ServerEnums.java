package com.juix.seckill.enums;

import lombok.Getter;

@Getter
public enum ServerEnums {
    SUCCESS(200, "登陆成功"),
    SERVER_ERROR(500, "服务器内部异常"),
    SERVER_NOT_IMPLEMENTED(501, "不支持的请求"),
    SERVER_UNAVAILABLE(503, "服务不可用"),
    SERVER_EXCEPTION(555, "服务器内部自定义异常"),
    SERVER_PARAMETER_ERROR(601, "参数校验异常: %s"),
    PHONE_NUMBER_NOT_EXIST(602, "当前手机号未注册！"),
    PASSWORD_EMPTY(603, "密码不能为空！"),
    SESSION_ERROR(604, "当前用户会话已失效！"),
    PASSWORD_ERROR(605, "当前账号或密码错误！"),
    ORDER_NOT_EXIST(606, "订单不存在！"),

    ;

    private int code;
    private String msg;

    ServerEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String fillParameter(Object... parameters) {
        String msg = String.format(SERVER_PARAMETER_ERROR.getMsg(), parameters);
        return msg;
    }
}
