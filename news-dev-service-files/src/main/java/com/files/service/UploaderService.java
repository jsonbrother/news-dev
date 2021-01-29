package com.files.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by TongHaiJun
 * 2021/1/21 21:53
 */
public interface UploaderService {

    /***
     * Fdfs文件上传
     * @param file 文件流
     * @param fileExtName 文件扩展名
     * @return 文件存放地址
     * @throws Exception 异常信息
     */
    String uploadFdfs(MultipartFile file, String fileExtName) throws Exception;

    /***
     * OSS文件上传
     * @param file 文件流
     * @param userId 用户的Id
     * @param fileExtName 文件扩展名
     * @return 文件存放地址
     * @throws Exception 异常信息
     */
    String uploadOss(MultipartFile file, String userId, String fileExtName) throws Exception;
}
