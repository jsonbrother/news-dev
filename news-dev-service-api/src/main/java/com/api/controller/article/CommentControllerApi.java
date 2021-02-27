package com.api.controller.article;

import com.pojo.bo.CommentReplyBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Json
 * @date 2021/2/15 23:28
 */
@Api(value = "文章评论相关业务", tags = "文章评论相关业务的Controller")
@RequestMapping("/comment")
public interface CommentControllerApi {

    @PostMapping("/createComment")
    @ApiOperation(value = "用户评论", notes = "用户评论", httpMethod = "POST")
    NewsJSONResult createArticle(@RequestBody @Valid CommentReplyBO commentReplyBO);

    @GetMapping("/counts")
    @ApiOperation(value = "用户评论数查询", notes = "用户评论数查询", httpMethod = "GET")
    NewsJSONResult commentCounts(@RequestParam String articleId);

    @GetMapping("/list")
    @ApiOperation(value = "查询文章的所有评论列表", notes = "查询文章的所有评论列表", httpMethod = "GET")
    NewsJSONResult list(@RequestParam String articleId,
                        @ApiParam(name = "page", value = "查询下一页的第几页") @RequestParam Integer page,
                        @ApiParam(name = "pageSize", value = "分页的每一页显示的条数") @RequestParam Integer pageSize);

    @PostMapping("/mng")
    @ApiOperation(value = "查询我的评论管理列表", notes = "查询我的评论管理列表", httpMethod = "POST")
    NewsJSONResult mng(@RequestParam String writerId,
                       @ApiParam(name = "page", value = "查询下一页的第几页") @RequestParam Integer page,
                       @ApiParam(name = "pageSize", value = "分页的每一页显示的条数") @RequestParam Integer pageSize);


    @PostMapping("/delete")
    @ApiOperation(value = "作者删除评论", notes = "作者删除评论", httpMethod = "POST")
    NewsJSONResult delete(@RequestParam String writerId, @RequestParam String commentId);

}
