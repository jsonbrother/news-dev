package com.api.controller.files;

import com.pojo.bo.NewAdminBO;
import com.result.NewsJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by TongHaiJun
 * 2021/1/22 20:21
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
}
