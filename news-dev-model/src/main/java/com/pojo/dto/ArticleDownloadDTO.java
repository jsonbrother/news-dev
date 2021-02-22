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
    private String MongoFileId;

    public ArticleDownloadDTO() {
    }

    public ArticleDownloadDTO(String articleId, String mongoFileId) {
        this.articleId = articleId;
        MongoFileId = mongoFileId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getMongoFileId() {
        return MongoFileId;
    }

    public void setMongoFileId(String mongoFileId) {
        MongoFileId = mongoFileId;
    }

    @Override
    public String toString() {
        return "ArticleDownloadDTO{" +
                "articleId='" + articleId + '\'' +
                ", MongoFileId='" + MongoFileId + '\'' +
                '}';
    }
}
