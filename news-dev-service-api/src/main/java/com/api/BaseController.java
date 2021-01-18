package com.api;

import com.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    protected static final String REDIS_USER_TOKEN = "redis_user_token";
    protected static final String REDIS_USER_INFO = "redis_user_info";

    @Value("${website.domain-name}")
    private String DOMAIN_NAME;

    protected static final Integer MOBILE_SMSCODE_EXPIRE = 30 * 60; // 半个小时
    protected static final Integer COOKIE_EXPIRE = 30 * 24 * 60 * 60; // 一个月

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

    /**
     * 设置cookie value编码utf-8
     *
     * @param response 请求对象
     * @param name     cookie的key
     * @param value    cookie的值
     * @param maxAge   过期时间
     */
    protected void setCookie(HttpServletResponse response, String name, String value, Integer maxAge) {
        try {
            value = URLEncoder.encode(value, "utf-8");
            setCookieValue(response, name, value, maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置cookie
     *
     * @param response 请求对象
     * @param name     cookie的key
     * @param value    cookie的值
     * @param maxAge   过期时间
     */
    private void setCookieValue(HttpServletResponse response, String name, String value, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DOMAIN_NAME); // 设置在具体域名之下
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
