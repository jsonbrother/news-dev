package com.admin.controller;

import com.admin.service.FriendLinkService;
import com.api.BaseController;
import com.api.controller.admin.FriendLinkControllerApi;
import com.enums.ResponseStatusEnum;
import com.pojo.bo.SaveFriendLinkBO;
import com.pojo.mo.FriendLinkMO;
import com.result.NewsJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Json
 * @date 2021/2/1 22:38
 */
@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {

    Logger logger = LoggerFactory.getLogger(FriendLinkController.class);

    private final FriendLinkService friendLinkService;

    @Autowired
    public FriendLinkController(FriendLinkService friendLinkService) {
        this.friendLinkService = friendLinkService;
    }


    @Override
    public NewsJSONResult saveOrUpdateFriendLink(SaveFriendLinkBO saveFriendLinkBO) {

        // 1.对象转换MO
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, friendLinkMO);
        friendLinkMO.setCreateTime(new Date());
        friendLinkMO.setUpdateTime(new Date());

        // 2.保存mongoDB
        friendLinkService.saveOrUpdateFriendLink(friendLinkMO);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult getFriendLinkList() {
        return NewsJSONResult.success(friendLinkService.queryAllFriendLinkList());
    }

    @Override
    public NewsJSONResult deleteFriendLink(String linkId) {

        // 1.判断请求参数
        if (StringUtils.isBlank(linkId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.FRIEND_LINK_DEL_ERROR);
        }

        // 2.删除链接信息
        friendLinkService.deleteFriendLink(linkId);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult queryPortalAllFriendLinkList() {
        return NewsJSONResult.success(friendLinkService.queryPortalAllFriendLinkList());
    }
}
