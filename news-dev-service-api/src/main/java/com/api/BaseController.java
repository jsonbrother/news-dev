package com.api;

import com.constant.CookieConstant;
import com.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
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
 * @author Json
 * @date 2021/1/16 17:01
 */
public class BaseController {

    @Autowired
    protected RedisOperator redis;

    @Value("${website.domain-name}")
    private String domainName;

    protected static final Integer COMMON_START_PAGE = 1;
    protected static final Integer COMMON_PAGE_SIZE = 10;

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
            value = URLEncoder.encode(value, "UTF-8");
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
        // 设置在具体域名之下
        cookie.setDomain(domainName);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response 请求对象
     * @param name     cookie的key
     */
    protected void delCookie(HttpServletResponse response, String name) {
        try {
            String delValue = URLEncoder.encode("", "UTF-8");
            setCookieValue(response, name, delValue, CookieConstant.COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected Integer getCountsFromRedis(String key) {
        String countsStr = redis.get(key);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }
}
