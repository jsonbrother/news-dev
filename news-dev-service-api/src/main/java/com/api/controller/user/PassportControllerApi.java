package com.api.controller.user;

import com.pojo.bo.RegistLoginBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:17
 */
@Api(value = "用户注册登陆的Controller", tags = "用户注册登陆的Controller")
@RequestMapping("passport")
public interface PassportControllerApi {

    @ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    NewsJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

    @ApiOperation(value = "一键注册登陆接口", notes = "一键注册登陆接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    NewsJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBO,
                           BindingResult result, HttpServletRequest request, HttpServletResponse response);

    @ApiOperation(value = "用户退出登陆", notes = "用户退出登陆", httpMethod = "POST")
    @PostMapping("/logout")
    NewsJSONResult logout(@RequestBody String userId, HttpServletRequest request, HttpServletResponse response);

}
