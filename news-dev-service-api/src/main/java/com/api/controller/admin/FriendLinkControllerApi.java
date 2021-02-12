package com.api.controller.admin;

import com.pojo.bo.SaveFriendLinkBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Json
 * @date 2021/2/1 22:13
 */
@Api(value = "首页友情链接维护", tags = "首页友情链接维护的Controller")
@RequestMapping("/friendLinkMng")
public interface FriendLinkControllerApi {

    @ApiOperation(value = "保存或修改友情链接", notes = "保存或修改友情链接", httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    NewsJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO, BindingResult result);

    @ApiOperation(value = "查询友情链接列表", notes = "查询友情链接列表", httpMethod = "POST")
    @PostMapping("/getFriendLinkList")
    NewsJSONResult getFriendLinkList();

    @ApiOperation(value = "删除友情链接", notes = "删除友情链接", httpMethod = "POST")
    @PostMapping("/deleteFriendLink")
    NewsJSONResult deleteFriendLink(@RequestParam String linkId);

    @ApiOperation(value = "门户端查询友情链接", notes = "门户端查询友情链接", httpMethod = "POST")
    @GetMapping("/portal/list")
    NewsJSONResult queryPortalAllFriendLinkList();
}
