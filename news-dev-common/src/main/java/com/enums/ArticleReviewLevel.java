package com.enums;

/**
 * 文章自动审核结果枚举
 * <p>
 * Created by TongHaiJun
 * 2021/1/24 15:02
 */
public enum ArticleReviewLevel {

    PASS("pass", "自动审核通过"),

    BLOCK("block", "自动审核不通过"),

    REVIEW("review", "建议人工复审");

    public final String type;
    public final String value;

    ArticleReviewLevel(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
