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
     * @param tempStatus
     * @return
     */
    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == INACTIVE.type || tempStatus == ACTIVE.type || tempStatus == FROZEN.type) {
                return true;
            }
        }
        return false;
    }
}
