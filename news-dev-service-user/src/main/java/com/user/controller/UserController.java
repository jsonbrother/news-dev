package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.UserControllerApi;
import com.enums.ResponseStatusEnum;
import com.pojo.AppUser;
import com.pojo.bo.UpdateUserInfoBO;
import com.pojo.vo.AppUserVO;
import com.pojo.vo.UserAccountInfoVO;
import com.result.NewsJSONResult;
import com.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by TongHaiJun
 * 2021/1/18 12:00
 */
@RestController
public class UserController extends BaseController implements UserControllerApi {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public NewsJSONResult getUserInfo(String userId) {

        // 1.判断参数 不能为空
        if (StringUtils.isBlank(userId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 2.根据userId查询信息
        AppUser appUser = getUser(userId);

        // 3.返回用户信息
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(appUser, appUserVO);

        return NewsJSONResult.success(appUserVO);
    }

    @Override
    public NewsJSONResult getAccountInfo(String userId) {

        // 1.判断参数 不能为空
        if (StringUtils.isBlank(userId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 2.根据userId查询信息
        AppUser appUser = getUser(userId);

        // 3.返回用户信息
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(appUser, accountInfoVO);

        return NewsJSONResult.success(accountInfoVO);
    }

    @Override
    public NewsJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return NewsJSONResult.errorMap(map);
        }

        // 2.执行更新操作
        userService.updateAppUserInfo(updateUserInfoBO);

        // 3.返回执行结果
        return NewsJSONResult.success();
    }

    private AppUser getUser(String userId) {
        // TODO 本方法后续公用 并且扩展
        return userService.getAppUser(userId);
    }

}
