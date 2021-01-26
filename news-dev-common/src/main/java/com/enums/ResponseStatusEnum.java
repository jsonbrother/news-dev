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
    UN_LOGIN(501, false, "请登录后再继续操作！"),
    TICKET_INVALID(502, false, "会话失效，请重新登录！"),
    NO_AUTH(503, false, "您的权限不足，无法继续操作！"),
    MOBILE_ERROR(504, false, "短信发送失败，请稍后重试！"),
    SMS_NEED_WAIT_ERROR(505, false, "短信发送太快，请稍后再试！"),
    SMS_CODE_ERROR(506, false, "验证码过期或不匹配，请稍后再试！"),
    USER_FROZEN(507, false, "用户已被冻结，请联系管理员！"),
    USER_UPDATE_ERROR(508, false, "用户信息更新失败，请联系管理员！"),
    USER_INACTIVE_ERROR(509, false, "请前往[账号设置]修改信息激活后再进行后续操作！"),
    FILE_UPLOAD_NULL_ERROR(510, false, "文件不能为空，请选择一个文件再上传！"),
    FILE_UPLOAD_FAILD(511, false, "文件上传失败！"),
    FILE_FORMATTER_FAILD(512, false, "文件图片格式不支持！"),
    FILE_MAX_SIZE_ERROR(513, false, "仅支持500kb大小以下的图片上传！"),
    FILE_NOT_EXIST_ERROR(514, false, "你所查看的文件不存在！"),

    // admin 管理系统 56x
    ADMIN_USERNAME_NULL_ERROR(561, false, "管理员登录名不能为空！"),
    ADMIN_USERNAME_EXIST_ERROR(562, false, "管理员登录名已存在！"),
    ADMIN_NAME_NULL_ERROR(563, false, "管理员负责人不能为空！"),
    ADMIN_PASSWORD_ERROR(564, false, "密码不能为空或者两次输入不一致！"),
    ADMIN_CREATE_ERROR(565, false, "添加管理员失败！"),
    ADMIN_PASSWORD_NULL_ERROR(566, false, "密码不能为空！"),
    ADMIN_NOT_EXIT_ERROR(567, false, "管理员不存在或密码错误！");


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
