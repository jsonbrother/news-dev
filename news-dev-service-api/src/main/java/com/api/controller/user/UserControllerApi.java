package com.api.controller.user;

import com.pojo.bo.UpdateUserInfoBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Json
 * @date 2021/1/18 11:57
 */
@Api(value = "用户信息相关Controller", tags = "用户信息相关Controller")
@RequestMapping("/user")
public interface UserControllerApi {

    @ApiOperation(value = "获得用户基本信息", notes = "获得用户基本信息", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    NewsJSONResult getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "获得用户账户信息", notes = "获得用户账户信息", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    NewsJSONResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "完善用户账户信息", notes = "完善用户账户信息", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    NewsJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);

    @ApiOperation(value = "根据用户的ids查询用户列表", notes = "根据用户的ids查询用户列表", httpMethod = "GET")
    @GetMapping("/queryByIds")
    NewsJSONResult queryByIds(@RequestParam String userIds);
}
