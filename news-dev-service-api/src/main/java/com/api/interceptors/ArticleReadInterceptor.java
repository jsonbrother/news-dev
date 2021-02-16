package com.api.interceptors;

import com.constant.RedisConstant;
import com.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文章阅读拦截器
 *
 * @author Json
 * @date 2021/2/15 16:45
 */
public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {
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

        String articleId = request.getParameter("articleId");

        String userIp = IPUtil.getRequestIp(request);
        boolean isExist = redis.keyIsExist(RedisConstant.REDIS_ALREADY_READ + ":" + articleId + ":" + userIp);

        return !isExist;
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
