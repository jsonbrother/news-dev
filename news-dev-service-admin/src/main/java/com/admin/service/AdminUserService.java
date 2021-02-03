package com.admin.service;

import com.pojo.AdminUser;
import com.pojo.bo.NewAdminBO;
import com.result.PagedGridResult;

import java.util.List;

/**
 * @author Json
 * @date 2021/1/24 20:02
 */
public interface AdminUserService {

    /***
     * 获得管理员的信息
     * @param username 用户名
     * @return 管理员信息
     */
    AdminUser queryAdminByUsername(String username);

    /***
     * 新增管理员
     * @param newAdminBO 新增管理员信息
     */
    void saveAdminUser(NewAdminBO newAdminBO);

    /***
     * 分页查询管理员列表
     * @param page 第几页
     * @param pageSize 每页条数
     * @return 管理员列表
     */
    PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
