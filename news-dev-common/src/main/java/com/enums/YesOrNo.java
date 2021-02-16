package com.enums;

/**
 * 是否枚举
 *
 * @author Json
 * @date 2021/2/10 20:14
 */
public enum YesOrNo {

    NO(0, "否"),
    YES(1, "是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
