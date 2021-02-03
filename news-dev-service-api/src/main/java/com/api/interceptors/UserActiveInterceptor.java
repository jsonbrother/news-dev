package com.api.interceptors;

import com.enums.ResponseStatusEnum;
import com.enums.UserStatus;
import com.exception.NewsException;
import com.pojo.AppUser;
import com.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户激活状态检查拦截器
 * <p>发文章，修改文章，删除文章
 * <p>发表评论，查看评论等等
 * <p>这些接口都是需要在用户激活以后，才能进行
 * <p>否则需要提示用户前往[账号设置]去修改信息
 *
 * @author Json
 * @date 2021/1/19 21:47
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {

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
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser appUser;
        if (StringUtils.isNotBlank(userJson)) {
            appUser = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            NewsException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        assert appUser != null;
        if (appUser.getActiveStatus() == null
                || !appUser.getActiveStatus().equals(UserStatus.ACTIVE.type)) {
            NewsException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
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
