package com.user.mapper;

import com.my.mapper.MyMapper;
import com.pojo.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}