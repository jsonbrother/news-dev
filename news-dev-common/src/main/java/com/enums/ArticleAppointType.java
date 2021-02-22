package com.enums;

/**
 * 文章发布操作类型枚举
 *
 * @author Json
 * @date 2021/2/10 21:57
 */
public enum ArticleAppointType {

    TIMING(1, "文章定时发布 - 定时"),
    IMMEDIATELY(0, "文章立即发布 - 即时");

    public final Integer type;
    public final String value;

    ArticleAppointType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
