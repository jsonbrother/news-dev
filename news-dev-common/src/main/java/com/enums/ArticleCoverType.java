package com.enums;

/**
 * 文章封面类型枚举
 *
 * @author Json
 * @date 2021/2/10 20:26
 */
public enum ArticleCoverType {

    ONE_IMAGE(1, "单图"),
    WORDS(2, "纯文字");

    public final Integer type;
    public final String value;

    ArticleCoverType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
