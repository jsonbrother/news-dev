package com.pojo.bo;

import javax.validation.constraints.NotBlank;

/**
 * 添加管理员BO对象
 *
 * @author Json
 * @date 2021/1/25 21:42
 */
public class NewAdminBO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "管理员姓名不能为空")
    private String adminName;

    private String img64;

    private String faceId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getImg64() {
        return img64;
    }

    public void setImg64(String img64) {
        this.img64 = img64;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    @Override
    public String toString() {
        return "NewAdminBO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", adminName='" + adminName + '\'' +
                ", img64='" + img64 + '\'' +
                ", faceId='" + faceId + '\'' +
                '}';
    }
}
