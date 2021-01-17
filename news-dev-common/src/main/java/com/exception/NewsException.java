package com.exception;

import com.enums.ResponseStatusEnum;

/**
 * 新闻的处理异常 统一封装
 * <p>
 * Created by TongHaiJun
 * 2021/1/17 9:13
 */
public class NewsException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}
