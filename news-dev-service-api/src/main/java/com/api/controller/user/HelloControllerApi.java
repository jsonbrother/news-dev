package com.api.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Json
 * @date 2021/1/15 12:32
 */
@Api(value = "Controller的标题", tags = {"xx功能的Controller"})
public interface HelloControllerApi {

    @ApiOperation(value = "hello方法的接口", notes = "hello方法的接口", httpMethod = "GET")
    @GetMapping("/hello")
    Object hello();

}
