package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.PassportControllerApi;
import com.enums.ResponseStatusEnum;
import com.enums.UserStatus;
import com.pojo.AppUser;
import com.pojo.bo.RegistLoginBO;
import com.user.service.IUserService;
import com.utils.IPUtil;
import com.result.NewsJSONResult;
import com.utils.JsonUtils;
import com.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:21
 */
@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(PassportControllerApi.class);

    private final SMSUtils smsUtils;
    private final IUserService userService;

    @Autowired
    public PassportController(SMSUtils smsUtils, IUserService userService) {
        this.smsUtils = smsUtils;
        this.userService = userService;
    }

    @Override
    public NewsJSONResult getSMSCode(String mobile, HttpServletRequest request) {

        // 1.获取用户ip
        String userIp = IPUtil.getRequestIp(request);

        // 2.根据用户的ip进行限制 限制用户在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        // 3.生成手机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, random);

        logger.info("用户手机号:{},ip地址:{},短信验证码:{}", mobile, userIp, random);

        // 4.把验证码存入redis 用于后续进行验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, MOBILE_SMSCODE_EXPIRE);


        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult doLogin(@Valid RegistLoginBO registLoginBO,
                                  BindingResult result,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return NewsJSONResult.errorMap(map);
        }

        // 2.校验验证码是否匹配
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        String redisSMSCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisSMSCode) || !redisSMSCode.equals(smsCode)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 3.判断用户是否存在
        AppUser appUser = userService.queryMobileIsExist(mobile);
        if (appUser != null && appUser.getActiveStatus().equals(UserStatus.FROZEN.type)) {
            // 如果用户不为空 并且状态为冻结
            return NewsJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (appUser == null) {
            // 如果为空 则注册用户信息
            appUser = userService.saveAppUser(mobile);
        }

        // 4.保存用户会话的相关信息
        Integer userActiveStatus = appUser.getActiveStatus();
        if (!userActiveStatus.equals(UserStatus.FROZEN.type)) {
            // 保存token到redis
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN + ":" + appUser.getId(), uToken);
            redis.set(REDIS_USER_INFO + ":" + appUser.getId(), JsonUtils.objectToJson(appUser));

            // 保存用户id和token到cookie
            setCookie(response, "uid", appUser.getId(), COOKIE_EXPIRE);
            setCookie(response, "utoken", uToken, COOKIE_EXPIRE);
        }

        // 5.用户登陆或注册成功 需要删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 6.返回用户的状态码
        return NewsJSONResult.success(userActiveStatus);
    }

    @Override
    public NewsJSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {

        // 1.清除redis中的用户信息
        redis.del(REDIS_USER_TOKEN + ":" + userId);

        // 2.清楚cookie中的用户信息
        setCookie(response, "uid", "", COOKIE_DELETE);
        setCookie(response, "utoken", "", COOKIE_DELETE);

        return NewsJSONResult.success();
    }
}
