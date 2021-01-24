package com.files.controller;

import com.api.BaseController;
import com.api.controller.files.FileUploaderControllerApi;
import com.enums.ResponseStatusEnum;
import com.files.resource.FileResource;
import com.files.service.UploaderService;
import com.result.NewsJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by TongHaiJun
 * 2021/1/22 20:25
 */
@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    private final UploaderService uploaderService;
    private final FileResource fileResource;

    @Autowired
    public FileUploaderController(UploaderService uploaderService, FileResource fileResource) {
        this.uploaderService = uploaderService;
        this.fileResource = fileResource;
    }

    @Override
    public NewsJSONResult uploadFace(String userId, MultipartFile file) throws Exception {


        // 1.判断参数 不能为空
        if (StringUtils.isBlank(userId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        } else if (file == null) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        String path;
        // 2.获得文件上传的名称
        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
            String[] fileNameArr = fileName.split("\\.");
            String suffix = fileNameArr[fileNameArr.length - 1]; // 获得文件后缀
            // 判断后缀是否符合我们的预定义规范
            if (!suffix.equalsIgnoreCase("png") && !suffix.equalsIgnoreCase("jpg")
                    && !suffix.equalsIgnoreCase("jpeg")) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
            }

            // 3.执行上传
            // path = uploaderService.uploadFdfs(file, suffix);
            path = uploaderService.uploadOSS(file, userId, suffix);
        } else {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        logger.info("path = {}", path);

        // 4.获得文件上传地址
        String finalPath;
        if (StringUtils.isNotBlank(path)) {
            // finalPath = fileResource.getHost() + path;
            finalPath = fileResource.getOssHost() + path;
        } else {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        return NewsJSONResult.success(finalPath);
    }

}
