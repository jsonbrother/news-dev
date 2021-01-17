package com.api;

import com.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TongHaiJun
 * 2021/1/16 17:01
 */
public class BaseController {

    @Autowired
    protected RedisOperator redis;

    protected static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 获取BO中的错误信息
     */
    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            // 发送验证错误时间所对应的属性
            String field = error.getField();
            // 验证的错误信息
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        });

        return map;
    }
}
