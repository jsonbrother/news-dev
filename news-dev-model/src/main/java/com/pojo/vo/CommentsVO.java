package com.pojo.vo;

import java.util.Date;

/**
 * 评论VO对象
 *
 * @author Json
 * @date 2021/2/15 23:55
 */
public class CommentsVO {

    /**
     * 留言id
     */
    private String commentId;

    /**
     * 留言父id
     */
    private String fatherId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 留言用户id
     */
    private String commentUserId;

    /**
     * 留言用户真实名称
     */
    private String commentUserNickname;

    /**
     * 留言用户头像
     */
    private String commentUserFace;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言时间
     */
    private Date createTime;

    /**
     * 引用留言用户真实名称
     */
    private String quoteUserNickname;

    /**
     * 引用留言内容
     */
    private String quoteContent;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserNickname() {
        return commentUserNickname;
    }

    public void setCommentUserNickname(String commentUserNickname) {
        this.commentUserNickname = commentUserNickname;
    }

    public String getContent() {
        return content;
    }

    public String getCommentUserFace() {
        return commentUserFace;
    }

    public void setCommentUserFace(String commentUserFace) {
        this.commentUserFace = commentUserFace;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getQuoteUserNickname() {
        return quoteUserNickname;
    }

    public void setQuoteUserNickname(String quoteUserNickname) {
        this.quoteUserNickname = quoteUserNickname;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    @Override
    public String toString() {
        return "CommentsVO{" +
                "commentId='" + commentId + '\'' +
                ", fatherId='" + fatherId + '\'' +
                ", articleId='" + articleId + '\'' +
                ", commentUserId='" + commentUserId + '\'' +
                ", commentUserNickname='" + commentUserNickname + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", quoteUserNickname='" + quoteUserNickname + '\'' +
                ", quoteContent='" + quoteContent + '\'' +
                '}';
    }
}
