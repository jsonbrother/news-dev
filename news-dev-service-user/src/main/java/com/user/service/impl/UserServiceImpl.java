package com.user.service.impl;

import com.enums.Sex;
import com.enums.UserStatus;
import com.pojo.AppUser;
import com.user.mapper.AppUserMapper;
import com.user.service.IUserService;
import com.utils.DateUtil;
import com.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * Created by TongHaiJun
 * 2021/1/17 10:23
 */
@Service
public class UserServiceImpl implements IUserService {

    private final AppUserMapper appUserMapper;
    private final Sid sid;

    @Autowired
    public UserServiceImpl(AppUserMapper appUserMapper, Sid sid) {
        this.appUserMapper = appUserMapper;
        this.sid = sid;
    }


    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile", mobile);
        return appUserMapper.selectOneByExample(userExample);
    }

    // TODO 待优化 统一事务管理
    @Transactional
    @Override
    public AppUser saveAppUser(String mobile) {
        AppUser appUser = new AppUser();
        String userId = sid.nextShort(); // 数据库主键id保证全局（全库）唯一
        appUser.setId(userId);
        appUser.setMobile(mobile);
        appUser.setNickName("用户:" + DesensitizationUtil.commonDisplay(mobile));
        appUser.setFace("");

        appUser.setBirthday(DateUtil.stringToDate("1900-01-01"));
        appUser.setSex(Sex.secret.type);
        appUser.setActiveStatus(UserStatus.INACTIVE.type);

        appUser.setTotalIncome(0);
        appUser.setCreatedTime(new Date());
        appUser.setUpdatedTime(new Date());
        appUserMapper.insert(appUser);

        return appUser;
    }
}
