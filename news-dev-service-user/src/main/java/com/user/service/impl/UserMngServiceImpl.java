package com.user.service.impl;

import com.api.service.BaseService;
import com.enums.UserStatus;
import com.github.pagehelper.PageHelper;
import com.pojo.AppUser;
import com.result.PagedGridResult;
import com.user.mapper.AppUserMapper;
import com.user.service.UserMngService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Json
 * @date 2021/2/6 16:17
 */
@Service
public class UserMngServiceImpl extends BaseService implements UserMngService {

    private final AppUserMapper appUserMapper;

    @Autowired
    public UserMngServiceImpl(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    @Override
    public PagedGridResult queryAllUserList(String nickName, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {

        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(nickName)) {
            criteria.andLike("nickName", "%" + nickName + "%");
        }

        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus", status);
        }

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);

        return setterPagedGrid(list, page);
    }

    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        appUserMapper.updateByPrimaryKeySelective(user);
    }
}
