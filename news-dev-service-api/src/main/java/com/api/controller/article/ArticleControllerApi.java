package com.api.controller.article;

import com.pojo.bo.NewArticleBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author Json
 * @date 2021/2/10 19:54
 */
@Api(value = "文章业务维护", tags = "文章业务维护的Controller")
@RequestMapping("/article")
public interface ArticleControllerApi {

    @ApiOperation(value = "用户发表文章", notes = "用户发表文章", httpMethod = "POST")
    @PostMapping("/createArticle")
    NewsJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO, BindingResult result);

    @PostMapping("queryMyList")
    @ApiOperation(value = "查询用户的所有文章列表", notes = "查询用户的所有文章列表", httpMethod = "POST")
    NewsJSONResult queryMyList(@RequestParam String userId, @RequestParam String keyword, @RequestParam Integer status,
                               @RequestParam Date startDate, @RequestParam Date endDate,
                               @RequestParam Integer page, @RequestParam Integer pageSize);

    @PostMapping("queryAllList")
    @ApiOperation(value = "管理员查询用户的所有文章列表", notes = "管理员查询用户的所有文章列表", httpMethod = "POST")
    NewsJSONResult queryAllList(@RequestParam Integer status,
                                @ApiParam(name = "page", value = "查询下一页的第几页")
                                @RequestParam Integer page,
                                @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
                                @RequestParam Integer pageSize);

    @PostMapping("doReview")
    @ApiOperation(value = "管理员对文章进行审核通过或者失败", notes = "管理员对文章进行审核通过或者失败", httpMethod = "POST")
    NewsJSONResult doReview(@RequestParam String articleId, @RequestParam Integer passOrNot);

    @PostMapping("/delete")
    @ApiOperation(value = "用户删除文章", notes = "用户删除文章", httpMethod = "POST")
    NewsJSONResult delete(@RequestParam String userId, @RequestParam String articleId);

    @PostMapping("/withdraw")
    @ApiOperation(value = "用户撤回文章", notes = "用户撤回文章", httpMethod = "POST")
    NewsJSONResult withdraw(@RequestParam String userId, @RequestParam String articleId);
}
