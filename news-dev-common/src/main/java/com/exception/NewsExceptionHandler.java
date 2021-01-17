package com.exception;

import com.result.NewsJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常拦截处理
 * <p>目的：可以针对异常的类型进行捕获 然后返回json信息到前端
 * Created by TongHaiJun
 * 2021/1/17 9:21
 */
@ControllerAdvice
public class NewsExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public NewsJSONResult returnMyException(MyCustomException e) {
        // 打印异常信息
        e.printStackTrace();
        return NewsJSONResult.exception(e.getResponseStatusEnum());
    }

}
