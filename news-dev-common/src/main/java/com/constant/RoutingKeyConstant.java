package com.constant;

/**
 * RabbitMQ路由键
 *
 * @author Json
 * @date 2021/2/19 23:06
 */
public class RoutingKeyConstant {

    public RoutingKeyConstant() {
    }

    /**
     * 文章HTML下载消息
     */
    public static final String ARTICLE_DOWNLOAD_DO = "article.download.do";

    /**
     * 文章HTML删除消息
     */
    public static final String ARTICLE_DELETE_DO = "article.delete.do";

    /**
     * 文章发布延时显示
     */
    public static final String PUBLISH_DELAY_DISPLAY = "publish.delay.display";

}
