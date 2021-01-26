package com.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员会话拦截器
 * <p>
 * Created by TongHaiJun
 * 2021/1/24 21:56
 */
public class AdminTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {


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

        String adminUserId = request.getHeader("adminUserId");
        String adminUserToken = request.getHeader("adminUserToken");

        // 判断是否放行
        boolean run = verifyUserIdToken(adminUserId, adminUserToken, REDIS_ADMIN_TOKEN);

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
