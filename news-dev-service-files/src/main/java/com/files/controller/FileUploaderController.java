package com.files.controller;

import com.api.controller.files.FileUploaderControllerApi;
import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.files.resource.FileResource;
import com.files.service.UploaderService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import com.utils.FileUtils;
import com.utils.extend.AliImageReviewUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
                return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAIL);
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
            return NewsJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAIL);
        }

        return NewsJSONResult.success(finalPath);
    }

    @Override
    public NewsJSONResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {

        // 1.声明list 用户存放多个图片的地址路径 返回到前端
        List<String> imageUrlList = new ArrayList<>();

        // 2.循环获得文件上传的名称
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String path = "";
                if (file != null) {
                    // 3.获得文件上传的名称
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        String[] fileNameArr = fileName.split("\\.");
                        // 获得文件后缀
                        String suffix = fileNameArr[fileNameArr.length - 1];
                        // 判断后缀是否符合我们的预定义规范
                        if (!PICTURE_SUFFIX_PNG.equalsIgnoreCase(suffix) && !PICTURE_SUFFIX_JPG.equalsIgnoreCase(suffix)
                                && !PICTURE_SUFFIX_JPEG.equalsIgnoreCase(suffix)) {
                            continue;
                        }

                        // 4.执行上传
                        path = uploaderService.uploadOss(file, userId, suffix);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                // 4.获得文件上传地址
                String finalPath;
                if (StringUtils.isNotBlank(path)) {
                    // finalPath = fileResource.getHost() + path;
                    finalPath = fileResource.getOssHost() + path;
                    // FIXME: 放入到imageUrlList之前，需要对图片做一次审核
                    imageUrlList.add(finalPath);
                }
            }
        }

        return NewsJSONResult.success(imageUrlList);
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

    @Override
    public void readInGridFs(String faceId, HttpServletResponse response) throws Exception {

        // 1.判断人脸Id参数
        if (StringUtils.isBlank(faceId) || "null".equals(faceId)) {
            NewsException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 2.从gridFS中读取
        File adminFile = readGridFsByFaceId(faceId);

        // 3.把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response, adminFile);
    }

    @Override
    public NewsJSONResult readFace64InGridFs(String faceId) throws Exception {

        // 1.获得gridFs中人脸文件
        File file = readGridFsByFaceId(faceId);

        // 2.人脸文件转换base64
        String base64Face = FileUtils.fileToBase64(file);

        // 3.返回人脸base64
        return NewsJSONResult.success(base64Face);
    }

    private static final String FAILED_IMAGE_URL = "file:/E:/idea%20product/faild.jpeg";

    /**
     * 阿里云图片自动审核i
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

    /**
     * 根据faceId从gridFS获取文件
     */
    private File readGridFsByFaceId(String faceId) throws Exception {


        // 根据faceId获取文件
        ObjectId objectId = new ObjectId(faceId);
        GridFSFindIterable gridFSFiles = gridFSBucket.find(Filters.eq("_id", objectId));
        GridFSFile gridFSFile = gridFSFiles.first();
        if (gridFSFile == null) {
            NewsException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 获得文件名称
        String fileName = gridFSFile.getFilename();
        logger.info("人脸文件名称:{}", fileName);

        // 获取文件流 保存文件到本地或者服务器的临时目录
        File fileTemp = new File("E:\\tmp\\news-dev\\face\\");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File file = new File("E:\\tmp\\news-dev\\face\\" + fileName);

        // 创建文件输出流
        OutputStream os = new FileOutputStream(file);
        // 下载到服务器或者本地
        gridFSBucket.downloadToStream(objectId, os);

        return file;
    }


}
