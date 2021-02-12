package com.admin.repository;

import com.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/2 21:50
 */
@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO, String> {

    List<FriendLinkMO> getAllByIsDelete(Integer isDelete);
}
