package com.pojo.vo;

/**
 * 文章发布者VO对象
 *
 * @author Json
 * @date 2021/2/14 0:45
 */
public class PublisherVO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 真实姓名
     */
    private String nickName;

    /**
     * 人脸信息
     */
    private String face;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

}
