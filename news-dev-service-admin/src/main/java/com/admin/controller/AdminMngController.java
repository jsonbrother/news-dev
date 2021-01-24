package com.admin.controller;

import com.admin.service.AdminUserService;
import com.api.BaseController;
import com.api.controller.admin.AdminMngControllerApi;
import com.enums.ResponseStatusEnum;
import com.pojo.AdminUser;
import com.pojo.bo.AdminLoginBO;
import com.result.NewsJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * Created by TongHaiJun
 * 2021/1/24 20:17
 */
@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    private final Logger logger = LoggerFactory.getLogger(AdminMngController.class);

    private final AdminUserService adminUserService;

    @Autowired
    public AdminMngController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public NewsJSONResult adminLogin(AdminLoginBO adminLoginBO, BindingResult result, HttpServletResponse response) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return NewsJSONResult.errorMap(map);
        }

        // 2.查询admin用户的信息
        AdminUser adminUser = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());

        // 3.判断登陆信息
        if (adminUser == null) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (isPwdMatch) {
            doLoginSettings(adminUser, response); // 保存admin信息到浏览器
            return NewsJSONResult.success();
        } else {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    /***
     * 用户admin用户登陆过后的基本信息设置
     * @param adminUser admin用户信息
     * @param response  响应对象
     */
    private void doLoginSettings(AdminUser adminUser, HttpServletResponse response) {
        // 保存token放入到redis中
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_INFO + ":" + adminUser.getId(), token);

        // 保存admin登陆基本token信息到cookie中
        setCookie(response, "atoken", token, COOKIE_EXPIRE);
        setCookie(response, "aid", adminUser.getId(), COOKIE_EXPIRE);
        setCookie(response, "aname", adminUser.getAdminName(), COOKIE_EXPIRE);
    }

}
