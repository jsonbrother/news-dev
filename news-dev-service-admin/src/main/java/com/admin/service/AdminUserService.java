package com.admin.service;

import com.pojo.AdminUser;

/**
 * Created by TongHaiJun
 * 2021/1/24 20:02
 */
public interface AdminUserService {

    /***
     * 获得管理员的信息
     * @param username 用户名
     * @return 管理员信息
     */
    AdminUser queryAdminByUsername(String username);
}
