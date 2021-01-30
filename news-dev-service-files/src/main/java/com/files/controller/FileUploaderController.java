package com.files.controller;

import com.api.controller.files.FileUploaderControllerApi;
import com.enums.ResponseStatusEnum;
import com.files.resource.FileResource;
import com.files.service.UploaderService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import com.utils.extend.AliImageReviewUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;

/**
 * Created by TongHaiJun
 * 2021/1/22 20:25
 */
@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    private final UploaderService uploaderService;
    private final FileResource fileResource;
    private final AliImageReviewUtils aliImageReviewUtils;
    private final GridFSBucket gridFSBucket;

    private static final String PICTURE_SUFFIX_PNG = "png";
    private static final String PICTURE_SUFFIX_JPG = "jpg";
    private static final String PICTURE_SUFFIX_JPEG = "jpeg";

    @Autowired
    public FileUploaderController(UploaderService uploaderService, FileResource fileResource,
                                  AliImageReviewUtils aliImageReviewUtils, GridFSBucket gridFSBucket) {
        this.uploaderService = uploaderService;
        this.fileResource = fileResource;
        this.aliImageReviewUtils = aliImageReviewUtils;
        this.gridFSBucket = gridFSBucket;
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
            // 获得文件后缀
            String suffix = fileNameArr[fileNameArr.length - 1];
            // 判断后缀是否符合我们的预定义规范
            if (!PICTURE_SUFFIX_PNG.equalsIgnoreCase(suffix) && !PICTURE_SUFFIX_JPG.equalsIgnoreCase(suffix)
                    && !PICTURE_SUFFIX_JPEG.equalsIgnoreCase(suffix)) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
            }

            // 3.执行上传
            path = uploaderService.uploadOss(file, userId, suffix);
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

    @Override
    public NewsJSONResult uploadToGridFs(NewAdminBO newAdminBO) throws Exception {

        // 1.获得图片的base64字符串
        String file64 = newAdminBO.getImg64();

        // 2.将base64字符串转换成byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());

        // 3.转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        // 4.上传到gridFs中
        String imgName = newAdminBO.getUsername() + ".png";
        ObjectId objectId = gridFSBucket.uploadFromStream(imgName, inputStream);

        // 5.获得文件在gridFs中的主键
        String fileId = objectId.toString();

        return NewsJSONResult.success(fileId);
    }

    private static final String FAILED_IMAGE_URL = "file:/E:/idea%20product/faild.jpeg";

    /**
     * 阿里云图片自动审核
     */
    private String doAliImageReview(String pendingImageUrl) {

        /*
          fastdfs 默认存在于内网，无法被阿里云内容管理服务检查到
          需要配置到公网才行：
          1. 内网穿透，natppp/花生壳/ngrok
          2. 路由配置端口映射
          3. fdfs 发布到云服务器
         */
        boolean result = false;

        try {
            result = aliImageReviewUtils.reviewImage(pendingImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!result) {
            return FAILED_IMAGE_URL;
        }

        return pendingImageUrl;
    }

}
