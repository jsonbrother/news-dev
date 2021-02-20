package com.pojo.dto;

/**
 * 文章下载DTO对象
 *
 * @author Json
 * @date 2021/2/20 20:10
 */
public class ArticleDownloadDTO {

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 文章存储gridFs中的id
     */
    private String articleMongoId;

    public ArticleDownloadDTO() {
    }

    public String getArticleId() {
        return articleId;
    }

    public ArticleDownloadDTO(String articleId, String articleMongoId) {
        this.articleId = articleId;
        this.articleMongoId = articleMongoId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleMongoId() {
        return articleMongoId;
    }

    public void setArticleMongoId(String articleMongoId) {
        this.articleMongoId = articleMongoId;
    }

    @Override
    public String toString() {
        return "ArticleDownloadDTO{" +
                "articleId='" + articleId + '\'' +
                ", articleMongoId='" + articleMongoId + '\'' +
                '}';
    }
}
