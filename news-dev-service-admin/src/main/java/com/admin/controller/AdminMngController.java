package com.admin.controller;

import com.admin.service.AdminUserService;
import com.api.BaseController;
import com.api.controller.admin.AdminMngControllerApi;
import com.constant.AdminConstant;
import com.constant.CookieConstant;
import com.constant.RedisConstant;
import com.enums.FaceVerifyType;
import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.pojo.AdminUser;
import com.pojo.bo.AdminLoginBO;
import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.FaceVerifyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate;
    private final FaceVerifyUtils faceVerifyUtils;

    @Autowired
    public AdminMngController(AdminUserService adminUserService, RestTemplate restTemplate, FaceVerifyUtils faceVerifyUtils) {
        this.adminUserService = adminUserService;
        this.restTemplate = restTemplate;
        this.faceVerifyUtils = faceVerifyUtils;
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
            // 保存admin信息到浏览器
            doLoginSettings(adminUser, response);
            return NewsJSONResult.success();
        } else {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public NewsJSONResult adminIsExist(String username) {

        // 校验用户名是否唯一
        checkAdminExist(username);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult addNewAdmin(NewAdminBO newAdminBO, BindingResult result) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return NewsJSONResult.errorMap(map);
        }

        // 2.img64不为空 则代表人脸入库 否则需要哦用户输入密码和确认密码
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) ||
                    StringUtils.isBlank(newAdminBO.getConfirmPassword())
            ) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }

        // 3.密码不为空 则必须判断两次输入一致
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword()
                    .equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        // 4.校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());

        // 5.添加新的管理员
        adminUserService.saveAdminUser(newAdminBO);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult getAdminList(Integer page, Integer pageSize) {

        // 1.判断分页参数
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 2.查询管理员分页列表
        PagedGridResult result = adminUserService.queryAdminList(page, pageSize);

        return NewsJSONResult.success(result);
    }

    @Override
    public NewsJSONResult adminLogout(String adminId, HttpServletResponse response) {

        // 1.删除redis中admin的会话token
        redis.del(RedisConstant.REDIS_ADMIN_TOKEN + ":" + adminId);

        // 2.删除cookie中admin的信息
        delCookie(response, AdminConstant.ID);
        delCookie(response, AdminConstant.NAME);
        delCookie(response, AdminConstant.TOKEN);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult adminFaceLogin(AdminLoginBO adminLoginBO, HttpServletResponse response) {

        // 1.判断用户名和人脸信息
        if (StringUtils.isBlank(adminLoginBO.getUsername())) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isBlank(adminLoginBO.getImg64())) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

        // 2.获得数据库中faceId
        AdminUser adminUser = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        String adminFaceId = adminUser.getFaceId();
        if (StringUtils.isBlank(adminFaceId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }

        // 3.请求文件服务 获得人脸数据的base64
        String fileServiceUrlExecute = "http://files.news.com:8004/fs/readFace64InGridFS?faceId=" + adminFaceId;
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(fileServiceUrlExecute, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        String base64DB = bodyResult.getData().toString();

        // 4.调用阿里ai进行人脸对比识别 判断可信度
        boolean result = faceVerifyUtils.faceVerify(FaceVerifyType.BASE64.type,
                adminLoginBO.getImg64(),
                base64DB,
                60); // 测试 及格则通过
        if (!result) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }

        // 5.admin登陆后的数据设置
        doLoginSettings(adminUser, response);

        return NewsJSONResult.success();
    }

    /***
     * 用户admin用户登陆过后的基本信息设置
     * @param adminUser admin用户信息
     * @param response  响应对象
     */
    private void doLoginSettings(AdminUser adminUser, HttpServletResponse response) {
        // 保存token放入到redis中
        String token = UUID.randomUUID().toString();
        redis.set(RedisConstant.REDIS_ADMIN_TOKEN + ":" + adminUser.getId(), token);

        // 保存admin登陆基本token信息到cookie中
        setCookie(response, AdminConstant.ID, adminUser.getId(), CookieConstant.COOKIE_EXPIRE);
        setCookie(response, AdminConstant.NAME, adminUser.getUsername(), CookieConstant.COOKIE_EXPIRE);
        setCookie(response, AdminConstant.TOKEN, token, CookieConstant.COOKIE_EXPIRE);
    }

    /***
     * 检查admin用户名是否存在
     * @param username admin用户名
     */
    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.queryAdminByUsername(username);
        if (admin != null) {
            NewsException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

}
