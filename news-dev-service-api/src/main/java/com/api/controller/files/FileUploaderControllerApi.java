package com.api.controller.files;

import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Json
 * @date 2021/1/22 20:21
 */
@Api(value = "文件上传的Controller", tags = "文件上传的Controller")
@RequestMapping("/fs")
public interface FileUploaderControllerApi {

    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    NewsJSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;

    /**
     * 文件上传到mongodb的gridFS中
     *
     * @param newAdminBO 新管理员信息
     * @return 应答
     * @throws Exception 异常信息
     */
    @PostMapping("/uploadToGridFS")
    NewsJSONResult uploadToGridFs(@RequestBody NewAdminBO newAdminBO) throws Exception;

    /**
     * 从gridFS中读取图片内容
     *
     * @param faceId 人脸Id
     * @throws Exception 异常信息
     */
    @GetMapping("readInGridFS")
    void readInGridFs(@RequestParam String faceId, HttpServletResponse response) throws Exception;

    /**
     * 从gridFS中读取图片内容 并且返回base64
     *
     * @param faceId 人脸Id
     * @throws Exception 异常信息
     */
    @GetMapping("readFace64InGridFS")
    NewsJSONResult readFace64InGridFs(@RequestParam String faceId) throws Exception;
}
