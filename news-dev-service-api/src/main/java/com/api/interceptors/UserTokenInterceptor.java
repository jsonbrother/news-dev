package com.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户会话拦截器
 *
 * @author Json
 * @date 2021/1/19 21:28
 */
public class UserTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {

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

        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        // 判断是否放行
        boolean run = verifyUserIdToken(userId, userToken, REDIS_USER_TOKEN);

        return run;
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
