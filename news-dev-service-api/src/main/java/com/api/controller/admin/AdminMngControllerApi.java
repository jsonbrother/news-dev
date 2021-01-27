package com.api.controller.admin;

import com.pojo.bo.AdminLoginBO;
import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "查询admin用户名是否存在", notes = "查询admin用户名是否存在", httpMethod = "POST")
    @PostMapping("/adminIsExist")
    NewsJSONResult adminIsExist(@RequestParam String username);

    @ApiOperation(value = "创建admin", notes = "创建admin", httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    NewsJSONResult addNewAdmin(@RequestBody NewAdminBO newAdminBO, BindingResult result);

    @ApiOperation(value = "查询admin列表", notes = "查询admin列表", httpMethod = "POST")
    @PostMapping("/getAdminList")
    NewsJSONResult getAdminList(@ApiParam(name = "page", value = "第几页") @RequestParam Integer page,
                                @ApiParam(name = "pageSize", value = "每一页显示的条数") @RequestParam Integer pageSize);

    @ApiOperation(value = "admin退出登陆", notes = "admin退出登陆", httpMethod = "POST")
    @PostMapping("/adminLogout")
    NewsJSONResult adminLogout(@RequestParam String adminId, HttpServletResponse response);
}
