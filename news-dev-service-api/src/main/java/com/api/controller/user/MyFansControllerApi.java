package com.api.controller.user;

import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Json
 * @date 2021/2/13 19:27
 */
@Api(value = "粉丝管理", tags = {"粉丝管理功能的controller"})
@RequestMapping("/fans")
public interface MyFansControllerApi {

    @ApiOperation(value = "查询当前用户是否关注作家", notes = "查询当前用户是否关注作家", httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    NewsJSONResult isMeFollowThisWriter(@RequestParam String writerId, @RequestParam String fanId);

    @ApiOperation(value = "用户关注作家，成为粉丝", notes = "用户关注作家，成为粉丝", httpMethod = "POST")
    @PostMapping("/follow")
    NewsJSONResult follow(@RequestParam String writerId, @RequestParam String fanId);

    @ApiOperation(value = "取消关注，作家损失粉丝", notes = "取消关注，作家损失粉丝", httpMethod = "POST")
    @PostMapping("/unFollow")
    NewsJSONResult unFollow(@RequestParam String writerId, @RequestParam String fanId);

    @ApiOperation(value = "查询我的所有粉丝列表", notes = "查询我的所有粉丝列表", httpMethod = "POST")
    @PostMapping("/queryAll")
    NewsJSONResult queryAll(@RequestParam String writerId,
                            @ApiParam(name = "page", value = "查询下一页的第几页")
                            @RequestParam Integer page,
                            @ApiParam(name = "pageSize", value = "分页查询每一页显示的条数")
                            @RequestParam Integer pageSize);

    @ApiOperation(value = "查询男女粉丝数量", notes = "查询男女粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatio")
    NewsJSONResult queryRatio(@RequestParam String writerId);

    @ApiOperation(value = "根据地域查询粉丝数量", notes = "根据地域查询粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatioByRegion")
    NewsJSONResult queryRatioByRegion(@RequestParam String writerId);
}
