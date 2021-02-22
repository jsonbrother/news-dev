package com.user.mapper;

import com.my.mapper.MyMapper;
import com.pojo.AppUser;
import com.pojo.vo.PublisherVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {

    /**
     * 获得文章发布者列表
     */
    List<PublisherVO> getUserList(Map<String, Object> map);
}