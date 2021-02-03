package com.pojo.bo;

import com.validate.CheckUrl;

import javax.validation.constraints.NotBlank;

/**
 * 保存友情链接BO对象
 *
 * @author Json
 * @date 2021/2/1 22:14
 */
public class SaveFriendLinkBO {

    private String id;

    @NotBlank(message = "友情链接名不能为空")
    private String linkName;

    @NotBlank(message = "友情链接地址不能为空")
    @CheckUrl
    private String linkUrl;

    @NotBlank(message = "请选择保留或者删除")
    private String isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "SaveFriendLinkBO{" +
                "id='" + id + '\'' +
                ", linkName='" + linkName + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
