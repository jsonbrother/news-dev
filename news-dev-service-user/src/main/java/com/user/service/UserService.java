package com.user.service;

import com.pojo.AppUser;
import com.pojo.bo.UpdateUserInfoBO;
import com.pojo.vo.PublisherVO;

import java.util.List;

/**
 * Created by TongHaiJun
 * 2021/1/17 10:22
 */
public interface UserService {

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

    /***
     * 根据userId获得账户信息
     * @param userId userId
     * @return appUser
     */
    AppUser getAppUser(String userId);

    /***
     * 修改完善用户信息并激活
     * @param userInfoBO 用户信息
     */
    void updateAppUserInfo(UpdateUserInfoBO userInfoBO);

    /**
     * 根据用户id查询用户
     */
    public List<PublisherVO> getUserList(List<String> userIdList);
}
