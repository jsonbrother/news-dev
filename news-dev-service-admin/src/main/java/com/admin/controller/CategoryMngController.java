package com.admin.controller;

import com.admin.service.CategoryService;
import com.api.BaseController;
import com.api.controller.admin.CategoryMngControllerApi;
import com.constant.RedisConstant;
import com.enums.ResponseStatusEnum;
import com.pojo.Category;
import com.pojo.bo.SaveCategoryBO;
import com.result.NewsJSONResult;
import com.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Json
 * @date 2021/2/3 22:14
 */
@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    private final Logger logger = LoggerFactory.getLogger(CategoryMngController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryMngController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public NewsJSONResult saveOrUpdateCategory(@Valid SaveCategoryBO newCategoryBO, BindingResult result) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            return NewsJSONResult.errorMap(getErrors(result));
        }

        Category category = new Category();
        BeanUtils.copyProperties(newCategoryBO, category);
        // 2.id为空新增 不为空修改
        if (newCategoryBO.getId() == null) {
            // 查询新增的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(category.getName(), null);
            if (!isExist) {
                // 新增到数据库
                categoryService.createCategory(category);
            } else {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        } else {
            // 查询修改的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(category.getName(), newCategoryBO.getOldName());
            if (!isExist) {
                // 修改到数据库
                categoryService.modifyCategory(category);
            } else {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult getCatList() {
        List<Category> categoryList = categoryService.queryCategoryList();
        return NewsJSONResult.success(categoryList);
    }

    @Override
    public NewsJSONResult getCats() {
        // 1.先从redis中查询
        String allCatJson = redis.get(RedisConstant.REDIS_ALL_CATEGORY);

        List<Category> categoryList;
        if (StringUtils.isBlank(allCatJson)) { // 如果有则返回
            categoryList = categoryService.queryCategoryList();
            redis.set(RedisConstant.REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categoryList));
        } else { // 如果没有则查询数据库库后先放缓存再返回
            categoryList = JsonUtils.jsonToList(allCatJson, Category.class);
        }

        return NewsJSONResult.success(categoryList);
    }
}
