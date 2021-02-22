package com.maker.service;

/**
 * @author Json
 * @date 2021/2/20 19:58
 */
public interface ArticleMakerService {

    /**
     * 生成文章HTML
     *
     * @param articleId 文章id
     * @throws Exception 异常信息
     */
    void generate(String articleId) throws Exception;

    /**
     * 下载文章HTML
     *
     * @param articleId      文章id
     * @param mongoFileId 文章gridFsId
     * @throws Exception 异常信息
     */
    void download(String articleId, String mongoFileId) throws Exception;

    /**
     * 删除文章HTML
     *
     * @param articleId 文章id
     * @throws Exception 异常信息
     */
    void delete(String articleId);
}
