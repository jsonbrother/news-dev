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
    TICKET_INVALID(502, false, "会话失效，请重新登录！");

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
