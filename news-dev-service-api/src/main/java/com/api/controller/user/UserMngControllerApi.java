package com.api.controller.user;

import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author Json
 * @date 2021/2/6 16:04
 */
@Api(value = "用户管理维护", tags = "用户管理维护的Controller")
@RequestMapping("/appUser")
public interface UserMngControllerApi {

    @ApiOperation(value = "查询所有网站用户", notes = "查询所有网站用户", httpMethod = "POST")
    @PostMapping("/queryAll")
    NewsJSONResult queryAll(@RequestParam String nickName, @RequestParam Integer status, @RequestParam Date startDate,
                            @RequestParam Date endDate, @RequestParam Integer page, @RequestParam Integer pageSize);

    @ApiOperation(value = "查看用户的详细", notes = "查看用户的详细", httpMethod = "POST")
    @PostMapping("/userDetail")
    NewsJSONResult userDetail(@RequestParam String userId);

    @PostMapping("/freezeUserOrNot")
    @ApiOperation(value = "冻结用户或者解冻用户", notes = "冻结用户或者解冻用户", httpMethod = "POST")
    NewsJSONResult freezeUserOrNot(@RequestParam String userId, @RequestParam Integer doStatus);
}
