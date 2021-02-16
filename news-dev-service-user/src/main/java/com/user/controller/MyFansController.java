package com.user.controller;

import com.api.BaseController;
import com.api.controller.user.MyFansControllerApi;
import com.enums.Sex;
import com.pojo.vo.FansCountsVO;
import com.result.NewsJSONResult;
import com.user.service.MyFansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Json
 * @date 2021/2/13 19:29
 */
@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

    private final Logger logger = LoggerFactory.getLogger(MyFansController.class);

    private final MyFansService myFansService;

    @Autowired
    public MyFansController(MyFansService myFansService) {
        this.myFansService = myFansService;
    }


    @Override
    public NewsJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = myFansService.isMeFollowThisWriter(writerId, fanId);
        return NewsJSONResult.success(res);
    }

    @Override
    public NewsJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId, fanId);
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult unFollow(String writerId, String fanId) {
        myFansService.unFollow(writerId, fanId);
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        return NewsJSONResult.success(myFansService.queryMyFansList(writerId, page, pageSize));
    }

    @Override
    public NewsJSONResult queryRatio(String writerId) {
        int manCounts = myFansService.queryFansCounts(writerId, Sex.man);
        int womanCounts = myFansService.queryFansCounts(writerId, Sex.woman);

        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCounts);
        fansCountsVO.setWomanCounts(womanCounts);

        return NewsJSONResult.success(fansCountsVO);
    }

    @Override
    public NewsJSONResult queryRatioByRegion(String writerId) {
        return NewsJSONResult.success(myFansService.queryRegionRatioCounts(writerId));
    }
}
