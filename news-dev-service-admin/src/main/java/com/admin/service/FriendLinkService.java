package com.admin.service;

import com.pojo.mo.FriendLinkMO;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/2 21:56
 */
public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    /**
     * 查询友情链接
     */
    List<FriendLinkMO> queryAllFriendLinkList();

    /**
     * 删除友情链接
     */
    void deleteFriendLink(String linkId);

    /**
     * 门户查询友情链接
     */
    List<FriendLinkMO> queryPortalAllFriendLinkList();

}
