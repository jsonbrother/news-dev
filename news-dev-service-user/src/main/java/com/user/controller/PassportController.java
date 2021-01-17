package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.PassportControllerApi;
import com.constant.RedisConstant;
import com.utils.IPUtil;
import com.utils.ResultUtil;
import com.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:21
 */
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController implements PassportControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(PassportControllerApi.class);

    private final SMSUtils smsUtils;

    @Autowired
    public PassportController(SMSUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    @Override
    public ResultUtil getSMSCode(String mobile, HttpServletRequest request) {

        // 1.获取用户ip
        String userIp = IPUtil.getRequestIp(request);

        // 2.根据用户的ip进行限制 限制用户在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        // 3.生成手机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, random);

        logger.info("用户手机号:{},ip地址:{},短信验证码:{}", mobile, userIp, random);

        // 4.把验证码存入redis 用于后续进行验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, RedisConstant.EXPIRE);

        return ResultUtil.success();
    }
}
