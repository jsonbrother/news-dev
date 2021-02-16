package com.exception;

import com.enums.ResponseStatusEnum;

/**
 * 个人自定义异常
 * <p> 目的：统一处理异常信息
 * <p>      便于解耦 service与controller错误的解耦 不会被service返回的类型而限制
 * Created by TongHaiJun
 * 2021/1/17 9:13
 */
public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码为：" + responseStatusEnum.status() + "；具体异常信息为：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
