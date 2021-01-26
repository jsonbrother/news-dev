package com.admin.service.impl;

import com.admin.mapper.AdminUserMapper;
import com.admin.service.AdminUserService;
import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.AdminUser;
import com.pojo.bo.NewAdminBO;
import com.result.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * Created by TongHaiJun
 * 2021/1/24 20:04
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final Sid sid;

    @Autowired
    public AdminUserServiceImpl(AdminUserMapper adminUserMapper, Sid sid) {
        this.adminUserMapper = adminUserMapper;
        this.sid = sid;
    }


    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria adminCriteria = adminExample.createCriteria();
        adminCriteria.andEqualTo("username", username);
        return adminUserMapper.selectOneByExample(adminExample);
    }

    @Override
    public void saveAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();

        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());

        // 如果密码不为空 则需要加密密码 存入数据库
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(), BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }

        // 如果人脸上传以后 则有faceId 需要和admin信息关联存储入库
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())) {
            adminUser.setFaceId(newAdminBO.getFaceId());
        }

        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());

        int result = adminUserMapper.insert(adminUser);
        if (result != 1) {
            NewsException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example adminExample = new Example(AdminUser.class);
        adminExample.orderBy("createdTime").desc();

        PageHelper.startPage(page, pageSize);
        List<AdminUser> adminUserList = adminUserMapper.selectByExample(adminExample);

        return setterPagedGrid(adminUserList, page);
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);

        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setRecords(pageList.getPages());
        grid.setTotal(pageList.getTotal());

        return grid;
    }


}
