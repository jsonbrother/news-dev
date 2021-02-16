package com.pojo.vo;

import java.util.Date;

/**
 * 文章详情VO对象
 *
 * @author Json
 * @date 2021/2/12 17:18
 */
public class ArticleDetailVO {

    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 分类Id
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发布者
     */
    private String publishUserId;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布者姓名
     */
    private String publishUserName;

    /**
     * 阅读数
     */
    private Integer readCounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getReadCounts() {
        return readCounts;
    }

    public void setReadCounts(Integer readCounts) {
        this.readCounts = readCounts;
    }

    @Override
    public String toString() {
        return "ArticleDetailVO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", publishUserId='" + publishUserId + '\'' +
                ", publishTime=" + publishTime +
                ", content='" + content + '\'' +
                ", publishUserName='" + publishUserName + '\'' +
                ", readCounts=" + readCounts +
                '}';
    }
}
