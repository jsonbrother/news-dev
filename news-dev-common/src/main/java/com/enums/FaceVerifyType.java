package com.enums;

/**
 * 人脸比对类型 枚举
 * <p>
 * Created by TongHaiJun
 * 2021/1/31 18:05
 */
public enum FaceVerifyType {

    BASE64(1, "图片Base64对比"),
    IMAGE_URL(0, "URL图片地址对比");

    public final Integer type;
    public final String value;

    FaceVerifyType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
