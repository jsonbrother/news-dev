package com.user.service;

import com.enums.Sex;
import com.pojo.vo.RegionRatioVO;
import com.result.PagedGridResult;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/13 19:30
 */
public interface MyFansService {

    /**
     * 查询当前用户是否关注作家
     */
    boolean isMeFollowThisWriter(String writerId, String fanId);

    /**
     * 关注成为粉丝
     */
    void follow(String writerId, String fanId);

    /**
     * 粉丝取消关注
     */
    void unFollow(String writerId, String fanId);

    /**
     * 查询我的粉丝数
     */
    PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize);

    /**
     * 查询粉丝数
     */
    Integer queryFansCounts(String writerId, Sex sex);

    /**
     * 查询粉丝数
     */
    List<RegionRatioVO> queryRegionRatioCounts(String writerId);
}
