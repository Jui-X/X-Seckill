package com.juix.seckill.enums;

import lombok.Getter;

@Getter
public enum ServerEnums {
    SUCCESS(200, "登陆成功"),
    SERVER_ERROR(500, "服务器内部异常"),
    SERVER_NOT_IMPLEMENTED(501, "不支持的请求"),
    SERVER_UNAVAILABLE(503, "服务不可用"),
    SERVER_EXCEPTION(555, "服务器内部自定义异常"),
    REQUEST_ILLEGAL(600, "当前请求非法！"),
    ACCESS_LIMITED(601, "请求过于频繁！"),
    SERVER_PARAMETER_ERROR(602, "参数校验异常: %s"),
    PHONE_NUMBER_NOT_EXIST(603, "当前手机号未注册！"),
    PASSWORD_EMPTY(604, "密码不能为空！"),
    SESSION_ERROR(605, "当前用户会话已失效！"),
    PASSWORD_ERROR(606, "当前账号或密码错误！"),
    ORDER_NOT_EXIST(607, "订单不存在！"),
    HANDLING(608, "秒杀订单处理中...请稍后"),
    SEC_KILL_SUCCEED(609, "秒杀成功～"),
    SEC_KILL_RATE_LIMIT(610, "秒杀活动过热，请重试"),
    SEC_KILL_AGAIN(611, "已抢购过此商品，不能再次抢购~"),
    SEC_KILL_END(612, "当前秒杀活动已结束"),
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
