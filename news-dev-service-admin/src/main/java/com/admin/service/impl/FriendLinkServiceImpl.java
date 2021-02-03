package com.admin.service.impl;

import com.admin.repository.FriendLinkRepository;
import com.admin.service.FriendLinkService;
import com.pojo.mo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/2 21:56
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    private final FriendLinkRepository friendLinkRepository;

    @Autowired
    public FriendLinkServiceImpl(FriendLinkRepository friendLinkRepository) {
        this.friendLinkRepository = friendLinkRepository;
    }


    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO);
    }

    @Override
    public List<FriendLinkMO> queryAllFriendLinkList() {
        return friendLinkRepository.findAll();
    }

    @Override
    public void deleteFriendLink(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }
}
