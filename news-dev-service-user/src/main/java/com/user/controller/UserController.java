package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.UserControllerApi;
import com.constant.RedisConstant;
import com.enums.ResponseStatusEnum;
import com.pojo.AppUser;
import com.pojo.bo.UpdateUserInfoBO;
import com.pojo.vo.AppUserVO;
import com.pojo.vo.UserAccountInfoVO;
import com.result.NewsJSONResult;
import com.user.service.UserService;
import com.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by TongHaiJun
 * 2021/1/18 12:00
 */
@RestController
public class UserController extends BaseController implements UserControllerApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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

        // 4.查询redis中用户的关注数和粉丝数 放入userVO到前端渲染
        appUserVO.setMyFansCounts(getCountsFromRedis(RedisConstant.REDIS_WRITER_FANS_COUNTS + ":" + userId));
        appUserVO.setMyFollowCounts(getCountsFromRedis(RedisConstant.REDIS_MY_FOLLOW_COUNTS + ":" + userId));

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

    @Override
    public NewsJSONResult queryByIds(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        assert userIdList != null;
        List<AppUserVO> publisherList = userIdList.stream().map(this::getBasicUserInfo).collect(Collectors.toList());

        return NewsJSONResult.success(publisherList);
    }

    private AppUser getUser(String userId) {
        // 查询判断redis中是否存在该用户信息
        String userJson = redis.get(RedisConstant.REDIS_USER_INFO + ":" + userId);

        AppUser appUser;
        if (StringUtils.isNotBlank(userJson)) {
            appUser = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            appUser = userService.getAppUser(userId);
            // 数据存入redis中
            redis.set(RedisConstant.REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(appUser));
        }

        return appUser;
    }

    private AppUserVO getBasicUserInfo(String userId) {
        // 1. 根据userId查询用户的信息
        AppUser user = getUser(userId);

        // 2. 返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }
}
