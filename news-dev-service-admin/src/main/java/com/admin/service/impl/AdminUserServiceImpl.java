package com.admin.service.impl;

import com.admin.mapper.AdminUserMapper;
import com.admin.service.AdminUserService;
import com.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


/**
 * Created by TongHaiJun
 * 2021/1/24 20:04
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;

    @Autowired
    public AdminUserServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }


    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria adminCriteria = adminExample.createCriteria();
        adminCriteria.andEqualTo("username", username);
        return adminUserMapper.selectOneByExample(adminExample);
    }

}
