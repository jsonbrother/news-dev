package com.maker.service.impl;

import com.maker.service.ArticleMakerService;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Json
 * @date 2021/2/17 16:48
 */
@Service
public class ArticleMakerServiceImpl implements ArticleMakerService {

    private final GridFSBucket gridFSBucket;

    @Autowired
    public ArticleMakerServiceImpl(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public void download(String articleId, String articleMongoId) throws Exception {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        OutputStream outputStream = null;
        try {
            // 3.创建输出流
            outputStream = new FileOutputStream(file);

            // 4.执行下载
            gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);
        } finally {
            // 5.关闭输出流
            assert outputStream != null;
            outputStream.close();
        }
    }

    @Override
    public void delete(String articleId) throws Exception {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        // 3.删除文件
        file.delete();
    }
}
