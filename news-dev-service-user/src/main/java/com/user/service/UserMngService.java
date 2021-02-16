package com.user.service;

import com.result.PagedGridResult;

import java.util.Date;

/**
 * @author Json
 * @date 2021/2/6 16:17
 */
public interface UserMngService {

    /**
     * 查询管理员列表
     */
    public PagedGridResult queryAllUserList(String nickName, Integer status, Date startDate,
                                            Date endDate, Integer page, Integer pageSize);

    /**
     * 冻结用户账号，或者解除冻结状态
     */
    public void freezeUserOrNot(String userId, Integer doStatus);

}
