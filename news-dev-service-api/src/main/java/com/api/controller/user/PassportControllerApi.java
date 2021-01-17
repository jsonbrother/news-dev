package com.api.controller.user;

import com.pojo.bo.RegistLoginBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:17
 */
@Api(value = "用户注册登陆", tags = "用户注册登陆的controller")
public interface PassportControllerApi {

    @ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    NewsJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

    @ApiOperation(value = "一键注册登陆接口", notes = "一键注册登陆接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    NewsJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBO, BindingResult result);

}
