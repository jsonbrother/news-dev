package com.api.controller.article;

import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Json
 * @date 2021/2/12 17:09
 */
@Api(value = "门户端文章业务维护", tags = "门户端文章业务维护的Controller")
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {

    @GetMapping("/list")
    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
    NewsJSONResult list(@RequestParam String keyword,
                        @RequestParam Integer category,
                        @ApiParam(name = "page", value = "查询下一页的第几页")
                        @RequestParam Integer page,
                        @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
                        @RequestParam Integer pageSize);

    @GetMapping("hotList")
    @ApiOperation(value = "首页查询热闻列表", notes = "首页查询热闻列表", httpMethod = "GET")
    NewsJSONResult hotList();


    @GetMapping("queryArticleListOfWriter")
    @ApiOperation(value = "查询作家发布的所有文章列表", notes = "查询作家发布的所有文章列表", httpMethod = "GET")
    NewsJSONResult queryArticleListOfWriter(@RequestParam String writerId,
                                            @ApiParam(name = "page", value = "查询下一页的第几页")
                                            @RequestParam Integer page,
                                            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
                                            @RequestParam Integer pageSize);

    @GetMapping("queryGoodArticleListOfWriter")
    @ApiOperation(value = "作家页面查询近期佳文", notes = "作家页面查询近期佳文", httpMethod = "GET")
    NewsJSONResult queryGoodArticleListOfWriter(@RequestParam String writerId);

    @GetMapping("detail")
    @ApiOperation(value = "文章详情查询", notes = "文章详情查询", httpMethod = "GET")
    NewsJSONResult detail(@RequestParam String articleId);

    @PostMapping("readArticle")
    @ApiOperation(value = "阅读文章，文章阅读量累加", notes = "阅读文章，文章阅读量累加", httpMethod = "POST")
    NewsJSONResult readArticle(@RequestParam String articleId, HttpServletRequest request);
}
