package com.api.controller.user;

import com.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:17
 */
@Api(value = "用户注册登陆", tags = "用户注册登陆的controller")
public interface PassportControllerApi {

    @ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    ResultUtil getSMSCode(String mobile, HttpServletRequest request);

}
