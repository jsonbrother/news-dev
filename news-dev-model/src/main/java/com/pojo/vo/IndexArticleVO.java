package com.pojo.vo;

import java.util.Date;

/**
 * 索引文章VO对象
 *
 * @author Json
 * @date 2021/2/12 18:57
 */
public class IndexArticleVO {

    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章所属分类id
     */
    private Integer categoryId;

    /**
     * 文章类型
     */
    private Integer articleType;

    /**
     * 文章封面图
     */
    private String articleCover;

    /**
     * 是否是预约定时发布的文章
     */
    private Integer isAppoint;

    /**
     * 文章状态
     */
    private Integer articleStatus;

    /**
     * 发布者用户id
     */
    private String publishUserId;

    /**
     * 文章发布时间（也是预约发布的时间）
     */
    private Date publishTime;

    /**
     * 用户累计点击阅读数（喜欢数）（点赞）
     */
    private Integer readCounts;

    /**
     * 文章评论总数
     */
    private Integer commentCounts;

    private String mongoFileId;

    /**
     * 逻辑删除状态
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布者信息
     */
    private AppUserVO publisherVO;

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

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public Integer getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(Integer isAppoint) {
        this.isAppoint = isAppoint;
    }

    public Integer getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getReadCounts() {
        return readCounts;
    }

    public void setReadCounts(Integer readCounts) {
        this.readCounts = readCounts;
    }

    public Integer getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getMongoFileId() {
        return mongoFileId;
    }

    public void setMongoFileId(String mongoFileId) {
        this.mongoFileId = mongoFileId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppUserVO getPublisherVO() {
        return publisherVO;
    }

    public void setPublisherVO(AppUserVO publisherVO) {
        this.publisherVO = publisherVO;
    }
}
