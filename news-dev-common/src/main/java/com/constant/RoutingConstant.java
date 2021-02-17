package com.constant;

/**
 * 路由地址常量
 *
 * @author Json
 * @date 2021/2/17 18:05
 */
public class RoutingConstant {

    public RoutingConstant() {
    }

    /**
     * 根据用户的ids查询用户列表
     */
    public static final String USER_QUERY_BY_IDS = "http://user.news.com:8003/user/queryByIds";

    /**
     * 门户文章详细
     */
    public static final String PORTAL_ARTICLE_DETAIL = "http://www.news.com:8001/portal/article/detail";

    /**
     * 文章HTML下载
     */
    public static final String ARTICLE_MAKER_DOWNLOAD = "http://maker.news.com:8002/article/maker/download";

    /**
     * 文章HTML删除
     */
    public static final String ARTICLE_MAKER_DELETE = "http://maker.news.com:8002/article/maker/delete";
}
