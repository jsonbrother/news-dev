package com.api.interceptors;

import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by TongHaiJun
 * 2021/1/19 21:32
 */
public class BaseInterceptor {

    @Autowired
    protected RedisOperator redis;

    private static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";

    boolean verifyUserIdToken(String id, String token) {

        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
            String redisToken = redis.get(BaseInterceptor.REDIS_USER_TOKEN + ":" + id);
            if (StringUtils.isBlank(redisToken)) {
                NewsException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                if (!redisToken.equalsIgnoreCase(token)) {
                    NewsException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        } else {
            NewsException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        return true;
    }
}
