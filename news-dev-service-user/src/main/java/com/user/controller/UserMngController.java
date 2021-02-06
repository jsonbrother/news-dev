package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.UserMngControllerApi;
import com.constant.RedisConstant;
import com.enums.ResponseStatusEnum;
import com.enums.UserStatus;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.user.service.UserMngService;
import com.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Json
 * @date 2021/2/6 16:13
 */
@RestController
public class UserMngController extends BaseController implements UserMngControllerApi {

    private Logger logger = LoggerFactory.getLogger(UserMngController.class);

    private final UserMngService userMngService;
    private final UserService userService;

    @Autowired
    public UserMngController(UserMngService userMngService, UserService userService) {
        this.userMngService = userMngService;
        this.userService = userService;
    }


    @Override
    public NewsJSONResult queryAll(String nickName, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {

        // 1.判断分页参数
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = userMngService.queryAllUserList(nickName, status, startDate, endDate, page, pageSize);

        return NewsJSONResult.success(result);
    }

    @Override
    public NewsJSONResult userDetail(String userId) {
        return NewsJSONResult.success(userService.getAppUser(userId));
    }

    @Override
    public NewsJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.isUserStatusValid(doStatus)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        userMngService.freezeUserOrNot(userId, doStatus);

        // 刷新用户状态：
        // 1. 删除用户会话 从而保障用户需要重新登录以后再来刷新他的会话状态
        // 2. 查询最新用户的信息 重新放入redis中 做一次更新
        redis.del(RedisConstant.REDIS_USER_INFO + ":" + userId);

        return NewsJSONResult.success();
    }
}
