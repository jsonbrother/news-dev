package com.api.controller.admin;

import com.pojo.bo.AdminLoginBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by TongHaiJun
 * 2021/1/24 20:11
 */
@Api(value = "管理员admin维护", tags = "管理员admin维护的Controller")
@RequestMapping("/adminMng")
public interface AdminMngControllerApi {

    @ApiOperation(value = "管理员登陆", notes = "管理员登陆", httpMethod = "POST")
    @PostMapping("/adminLogin")
    NewsJSONResult adminLogin(@RequestBody AdminLoginBO adminLoginBO, BindingResult result, HttpServletResponse response);

}
