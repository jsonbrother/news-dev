package com.enums;

/**
 * 性别枚举
 * <p>
 * Created by TongHaiJun
 * 2021/1/17 10:56
 */
public enum Sex {
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
