package com.files.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by TongHaiJun
 * 2021/1/22 21:11
 */
@Component
@PropertySource("classpath:file-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "file")
public class FileResource {

    /**
     * fastdfs访问地址
     */
    private String host;

    /**
     * 阿里云OSS的外网访问
     */
    private String endpoint;

    /**
     * 阿里云OSS的Bucket名称
     */
    private String bucketName;

    /**
     * 阿里云OSS的目录对象
     */
    private String objectName;

    /**
     * 阿里云OSS的访问地址
     */
    private String ossHost;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOssHost() {
        return ossHost;
    }

    public void setOssHost(String ossHost) {
        this.ossHost = ossHost;
    }
}
