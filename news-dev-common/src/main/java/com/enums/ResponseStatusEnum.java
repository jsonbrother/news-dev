package com.enums;

/**
 * 响应结果枚举
 * <p>
 * Created by TongHaiJun
 * 2021/1/15 14:49
 */
public enum ResponseStatusEnum {

    SUCCESS(200, true, "成功"),
    FAILED(500, false, "失败"),

    // 50x
    TICKET_INVALID(502, false, "会话失效，请重新登录！"),
    NO_AUTH(503, false, "您的权限不足，无法继续操作！"),
    MOBILE_ERROR(504, false, "短信发送失败，请稍后重试！"),
    SMS_NEED_WAIT_ERROR(505, false, "短信发送太快，请稍后再试！"),
    SMS_CODE_ERROR(506, false, "验证码过期或不匹配，请稍后再试！"),
    USER_FROZEN(507, false, "用户已被冻结，请联系管理员！"),
    USER_UPDATE_ERROR(508, false, "用户信息更新失败，请联系管理员！");


    // 响应业务状态
    private Integer status;
    // 调用是否成功
    private Boolean success;
    // 响应消息 可以为成功或者失败的消息
    private String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }

    public Boolean success() {
        return success;
    }

    public String msg() {
        return msg;
    }
}
