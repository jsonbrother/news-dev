package com.user.service.impl;

import com.api.service.BaseService;
import com.constant.RedisConstant;
import com.enums.Sex;
import com.github.pagehelper.PageHelper;
import com.pojo.AppUser;
import com.pojo.Fans;
import com.pojo.vo.RegionRatioVO;
import com.result.PagedGridResult;
import com.user.mapper.FansMapper;
import com.user.service.MyFansService;
import com.user.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Json
 * @date 2021/2/13 19:30
 */
@Service
public class MyFansServiceImpl extends BaseService implements MyFansService {

    private final FansMapper fansMapper;
    private final UserService userService;
    private final Sid sid;

    @Autowired
    public MyFansServiceImpl(FansMapper fansMapper, UserService userService, Sid sid) {
        this.fansMapper = fansMapper;
        this.userService = userService;
        this.sid = sid;
    }

    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fan = new Fans();
        fan.setFanId(fanId);
        fan.setWriterId(writerId);

        int count = fansMapper.selectCount(fan);

        return count > 0;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {
        // 获得粉丝用户的信息
        AppUser fanInfo = userService.getAppUser(fanId);

        String fanPkId = sid.nextShort();

        Fans fans = new Fans();
        fans.setId(fanPkId);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);

        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickName());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());

        fansMapper.insert(fans);

        // redis 作家粉丝数累加
        redis.increment(RedisConstant.REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        // redis 当前用户的（我的）关注数累加
        redis.increment(RedisConstant.REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Transactional
    @Override
    public void unFollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);

        fansMapper.delete(fans);

        // redis 作家粉丝数累减
        redis.decrement(RedisConstant.REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        // redis 当前用户的（我的）关注数累减
        redis.decrement(RedisConstant.REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Override
    public PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Fans> list = fansMapper.select(fans);
        return setterPagedGrid(list, page);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);

        return fansMapper.selectCount(fans);
    }

    private static final String[] regions = {"北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林",
            "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南",
            "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);

        List<RegionRatioVO> list = new ArrayList<>();
        for (String r : regions) {
            fans.setProvince(r);
            Integer count = fansMapper.selectCount(fans);

            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(r);
            regionRatioVO.setValue(count);

            list.add(regionRatioVO);
        }

        return list;
    }
}
