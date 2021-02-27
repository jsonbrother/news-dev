package com.exception;

import com.result.NewsJSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 捕获请求参数检验错误异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public NewsJSONResult returnException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> map = getErrors(result);
        return NewsJSONResult.errorMap(map);
    }

    /**
     * 获取BO中的错误信息
     */
    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发送验证错误的时候所对应的某个属性
            String field = error.getField();
            // 验证的错误消息
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }

}
