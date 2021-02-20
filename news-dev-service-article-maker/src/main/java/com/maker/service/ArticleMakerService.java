package com.maker.service;

/**
 * @author Json
 * @date 2021/2/20 19:58
 */
public interface ArticleMakerService {

    /**
     * 下载文章HTML
     *
     * @param articleId      文章id
     * @param articleMongoId 文章gridFsId
     * @throws Exception 异常信息
     */
    void download(String articleId, String articleMongoId) throws Exception;

    /**
     * 删除文章HTML
     *
     * @param articleId 文章id
     * @throws Exception 异常信息
     */
    void delete(String articleId) throws Exception;
}
