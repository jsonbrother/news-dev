package com.constant;

/**
 * Created by TongHaiJun
 * 2021/1/29 19:51
 */
public class RedisConstant {

    private RedisConstant() {

    }

    /**
     * 短信Code
     */
    public static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 用户Token令牌
     */
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    /**
     * 用户信息
     */
    public static final String REDIS_USER_INFO = "redis_user_info";

    /**
     * 管理员Token令牌
     */
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

    /**
     * 文章分类信息
     */
    public static final String REDIS_ALL_CATEGORY = "redis_all_category";
}
