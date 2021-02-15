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

    /**
     * 作家粉丝数
     */
    public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";

    /**
     * 当前用户的（我的）关注数
     */
    public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    /**
     * 文章阅读数
     */
    public static final String REDIS_ARTICLE_READ_COUNTS = "redis_article_read_counts";

    /**
     * 文章已读标志
     */
    public static final String REDIS_ALREADY_READ = "redis_already_read";
}
