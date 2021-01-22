package com.files.service.impl;

import com.files.service.UploaderService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by TongHaiJun
 * 2021/1/21 21:54
 */
@Service
public class UploaderServiceImpl implements UploaderService {

    private final FastFileStorageClient fastFileStorageClient;

    @Autowired
    public UploaderServiceImpl(FastFileStorageClient fastFileStorageClient) {
        this.fastFileStorageClient = fastFileStorageClient;
    }

    @Override
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception {

        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(),
                fileExtName,
                null);

        return storePath.getFullPath();
    }

}
