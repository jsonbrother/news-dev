package com.enums;

/**
 * 用户状态枚举
 * <p>
 * 2021/1/17 10:57
 */
public enum UserStatus {

    INACTIVE(0, "未激活"),
    ACTIVE(1, "已激活"),
    FROZEN(2, "已冻结");

    public final Integer type;
    public final String value;

    UserStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 判断传入的用户状态是不是有效的值
     *
     * @param tempStatus 状态码
     * @return 应答
     */
    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            return tempStatus.equals(INACTIVE.type) || tempStatus.equals(ACTIVE.type) || tempStatus.equals(tempStatus == FROZEN.type);
        }
        return false;
    }
}
