package com.maker.controller;

import com.api.controller.article.ArticleMakerControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import com.result.NewsJSONResult;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Json
 * @date 2021/2/17 16:48
 */
@RestController
public class ArticleMakerController implements ArticleMakerControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(ArticleMakerController.class);

    private final GridFSBucket gridFSBucket;

    @Autowired
    public ArticleMakerController(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public NewsJSONResult download(String articleId, String articleMongoId) throws Exception {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        // 3.创建输出流
        OutputStream outputStream = new FileOutputStream(file);

        // 4.执行下载
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult delete(String articleId) throws Exception {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        // 3.删除文件
        file.delete();

        return NewsJSONResult.success();
    }
}
