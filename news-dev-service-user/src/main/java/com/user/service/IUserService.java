package com.user.service;

import com.pojo.AppUser;

/**
 * Created by TongHaiJun
 * 2021/1/17 10:22
 */
public interface IUserService {

    /***
     * 判断用户是否存在
     * @param mobile 手机号
     * @return appUser
     */
    AppUser queryMobileIsExist(String mobile);

    /***
     * 新增用户
     * @param mobile 手机号
     * @return appUser
     */
    AppUser saveAppUser(String mobile);

}
