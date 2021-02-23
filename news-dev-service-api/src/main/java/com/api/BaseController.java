package com.api;

import com.constant.CookieConstant;
import com.constant.RoutingConstant;
import com.pojo.vo.AppUserVO;
import com.result.NewsJSONResult;
import com.utils.JsonUtils;
import com.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Json
 * @date 2021/1/16 17:01
 */
public class BaseController {

    @Autowired
    protected RedisOperator redis;
    @Autowired
    protected RestTemplate restTemplate;

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


    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 发起远程调用 获得用户的基本信息
     *
     * @param idSet 用户id集合
     * @return 用户信息Map集合
     */
    protected Map<String, AppUserVO> getBasicUserMap(Set idSet) {
        // String userServerUrlExecute = RoutingConstant.USER_QUERY_BY_IDS + "?userIds=" + JsonUtils.objectToJson(idSet);

        String serviceId = "SERVICE-USER";
        List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceId);
        ServiceInstance userService = instanceList.get(0);

        String userServerUrlExecute
                = "http://" + userService.getHost() +
                ":"
                + userService.getPort()
                + "/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);

        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(userServerUrlExecute, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> userVOList = null;
        if (bodyResult != null && bodyResult.getStatus() == HttpStatus.OK.value()) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            userVOList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        assert userVOList != null;
        return userVOList.stream().collect(Collectors.toMap(AppUserVO::getId, Function.identity(), (k1, k2) -> k2));
    }

}
