package com.api.controller.article;


import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Json
 * @date 2021/2/17 16:31
 */
@Api(value = "文章静态化业务", tags = "文章静态化业务的Controller")
@RequestMapping("/article/maker")
public interface ArticleMakerControllerApi {

    @PostMapping("/download")
    @ApiOperation(value = "下载html", notes = "下载html", httpMethod = "GET")
    NewsJSONResult download(String articleId, String articleMongoId) throws Exception;

    @GetMapping("/delete")
    @ApiOperation(value = "删除html", notes = "删除html", httpMethod = "GET")
    NewsJSONResult delete(String articleId) throws Exception;

}
