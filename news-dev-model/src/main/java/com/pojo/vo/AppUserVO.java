package com.pojo.vo;

/**
 * AppUser信息VO对象
 *
 * @author Json
 * @date 2021/1/18 19:56
 */
public class AppUserVO {

    private String id;

    /**
     * 昵称，媒体号
     */
    private String nickName;

    /**
     * 头像
     */
    private String face;

    /**
     * 用户状态：0：未激活 1：已激活 2：已冻结。
     */
    private Integer activeStatus;

    /**
     * 我的关注数
     */
    private Integer myFollowCounts;

    /**
     * 我的粉丝数
     */
    private Integer myFansCounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getMyFollowCounts() {
        return myFollowCounts;
    }

    public void setMyFollowCounts(Integer myFollowCounts) {
        this.myFollowCounts = myFollowCounts;
    }

    public Integer getMyFansCounts() {
        return myFansCounts;
    }

    public void setMyFansCounts(Integer myFansCounts) {
        this.myFansCounts = myFansCounts;
    }

    @Override
    public String toString() {
        return "AppUserVO{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", face='" + face + '\'' +
                ", activeStatus=" + activeStatus +
                ", myFollowCounts=" + myFollowCounts +
                ", myFansCounts=" + myFansCounts +
                '}';
    }
}
