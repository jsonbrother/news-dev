package com.user.service.impl;

import com.constant.RedisConstant;
import com.enums.ResponseStatusEnum;
import com.enums.Sex;
import com.enums.UserStatus;
import com.exception.NewsException;
import com.pojo.AppUser;
import com.pojo.bo.UpdateUserInfoBO;
import com.user.mapper.AppUserMapper;
import com.user.service.UserService;
import com.utils.DateUtil;
import com.utils.DesensitizationUtil;
import com.utils.JsonUtils;
import com.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
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
public class UserServiceImpl implements UserService {

    private final AppUserMapper appUserMapper;
    private final Sid sid;
    private final RedisOperator redis;

    @Autowired
    public UserServiceImpl(AppUserMapper appUserMapper, Sid sid, RedisOperator redis) {
        this.appUserMapper = appUserMapper;
        this.sid = sid;
        this.redis = redis;
    }

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile", mobile);
        return appUserMapper.selectOneByExample(userExample);
    }

    @Transactional
    @Override
    public AppUser saveAppUser(String mobile) {
        AppUser appUser = new AppUser();

        // 数据库主键id保证全局（全库）唯一
        String userId = sid.nextShort();

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

    @Override
    public AppUser getAppUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateAppUserInfo(UpdateUserInfoBO userInfoBO) {
        String userId = userInfoBO.getId();
        // 保证双写一致 先删除redis中的数据 后更新数据库
        redis.del(RedisConstant.REDIS_USER_INFO + ":" + userId);

        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(userInfoBO, appUser);

        appUser.setUpdatedTime(new Date());
        appUser.setActiveStatus(UserStatus.ACTIVE.type);

        int result = appUserMapper.updateByPrimaryKeySelective(appUser);
        // 通过影响条数判断是否更新成功
        if (result != 1) {
            NewsException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }

        // 更新redis中的用户信息
        AppUser user = getAppUser(userId);
        redis.set(RedisConstant.REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));

        // 缓存双删策略 保证双写一致
        try {
            Thread.sleep(100);
            redis.del(RedisConstant.REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
