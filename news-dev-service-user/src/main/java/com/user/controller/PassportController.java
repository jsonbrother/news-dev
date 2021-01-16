package com.user.controller;

import com.api.controller.user.PassportControllerApi;
import com.utils.ResultUtil;
import com.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:21
 */
@RestController
public class PassportController implements PassportControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(PassportControllerApi.class);

    private final SMSUtils smsUtils;

    @Autowired
    public PassportController(SMSUtils smsUtils) {
        this.smsUtils = smsUtils;
    }

    @Override
    public ResultUtil getSMSCode() {

        String random = "123456789";
        smsUtils.sendSMS("17821230165", random);

        return ResultUtil.success();
    }
}
