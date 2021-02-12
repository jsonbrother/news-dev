package com.files.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.files.resource.FileResource;
import com.files.service.UploaderService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.utils.extend.AliyunResource;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by TongHaiJun
 * 2021/1/21 21:54
 */
@Service
public class UploaderServiceImpl implements UploaderService {

    private final FastFileStorageClient fastFileStorageClient;
    private final FileResource fileResource;
    private final AliyunResource aliyunResource;
    private final Sid sid;

    @Autowired
    public UploaderServiceImpl(FastFileStorageClient fastFileStorageClient, FileResource fileResource, AliyunResource aliyunResource, Sid sid) {
        this.fastFileStorageClient = fastFileStorageClient;
        this.fileResource = fileResource;
        this.aliyunResource = aliyunResource;
        this.sid = sid;
    }

    @Override
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception {

        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(),
                fileExtName,
                null);

        return storePath.getFullPath();
    }

    @Override
    public String uploadOss(MultipartFile file, String userId, String fileExtName) throws Exception {

        // Endpoint以上海为例，其它Region请按实际情况填写。
        String endpoint = fileResource.getEndpoint();
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = aliyunResource.getAccessKeyId();
        String accessKeySecret = aliyunResource.getAccessKeySecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 用户文件存放地址。
        String fileName = sid.nextShort();
        String userObjectName = fileResource.getObjectName() + "/" + userId + "/" + fileName + "." + fileExtName;

        // 上传网络流。
        InputStream inputStream = file.getInputStream();
        ossClient.putObject(fileResource.getBucketName(), userObjectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return userObjectName;
    }
}
