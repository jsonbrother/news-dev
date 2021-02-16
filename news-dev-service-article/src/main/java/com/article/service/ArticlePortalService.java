package com.article.service;

import com.pojo.Article;
import com.pojo.vo.ArticleDetailVO;
import com.result.PagedGridResult;

import java.util.List;

/**
 * @author Json
 * @date 2021/2/12 17:15
 */
public interface ArticlePortalService {

    /**
     * 首页查询文章列表
     */
    PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize);

    /**
     * 首页查询热闻列表
     */
    List<Article> queryHotList();

    /**
     * 查询作家发布的所有文章列表
     */
    PagedGridResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize);

    /**
     * 作家页面查询近期佳文
     */
    PagedGridResult queryGoodArticleListOfWriter(String writerId);

    /**
     * 查询文章详情
     */
    ArticleDetailVO queryDetail(String articleId);
}
