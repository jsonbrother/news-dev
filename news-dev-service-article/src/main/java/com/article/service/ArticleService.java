package com.article.service;

import com.pojo.Category;
import com.pojo.bo.NewArticleBO;
import com.result.PagedGridResult;

import java.util.Date;

/**
 * @author Json
 * @date 2021/2/10 20:02
 */
public interface ArticleService {

    /**
     * 发布文章
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

    /**
     * 更新定时发布为即时发布
     */
    void updateAppointToPublish();

    /**
     * 用户中心 - 查询我的文章列表
     */
    PagedGridResult queryMyArticleList(String userId, String keyword, Integer status,
                                       Date startDate, Date endDate, Integer page, Integer pageSize);

    /**
     * 更改文章的状态
     */
    void updateArticleStatus(String articleId, Integer pendingStatus);

    /**
     * 管理员查询文章列表
     */
    PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize);

    /**
     * 删除文章
     */
    void deleteArticle(String userId, String articleId);

    /**
     * 撤回文章
     */
    void withdrawArticle(String userId, String articleId);
}
