package com.api.interceptors;

import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.utils.IPUtil;
import com.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by TongHaiJun
 * 2021/1/17 8:41
 */
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redis;

    private static final String MOBILE_SMSCODE = "mobile:smscode";


    /**
     * 请求访问到controller之前
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  拦截程序
     * @return true：放行 false：拦截
     * @throws Exception 异常信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1.获取用户ip
        String userIp = IPUtil.getRequestIp(request);

        // 2.判断redis是否存在key
        boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);

        // 3.存在则拦截
        if (keyIsExist) {
            NewsException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        return true;
    }

    /**
     * 请求访问到controller之后 渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问到controller之后 渲染视图之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
