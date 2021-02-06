package com.api.controller.admin;

import com.pojo.bo.SaveCategoryBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author Json
 * @date 2021/2/3 22:06
 */
@Api(value = "文章分类维护", tags = "文章分类维护的controller")
@RequestMapping("/categoryMng")
public interface CategoryMngControllerApi {

    @PostMapping("saveOrUpdateCategory")
    @ApiOperation(value = "新增或修改分类", notes = "新增或修改分类", httpMethod = "POST")
    public NewsJSONResult saveOrUpdateCategory(@RequestBody @Valid SaveCategoryBO newCategoryBO, BindingResult result);

    @PostMapping("getCatList")
    @ApiOperation(value = "查询分类列表", notes = "查询分类列表", httpMethod = "POST")
    public NewsJSONResult getCatList();

    @GetMapping("getCats")
    @ApiOperation(value = "用户端查询分类列表", notes = "用户端查询分类列表", httpMethod = "GET")
    public NewsJSONResult getCats();

}
