package com.maker.service.impl;

import com.api.config.RabbitMQConfig;
import com.constant.RoutingConstant;
import com.constant.RoutingKeyConstant;
import com.maker.service.ArticleMakerService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.pojo.dto.ArticleDownloadDTO;
import com.pojo.vo.ArticleDetailVO;
import com.result.NewsJSONResult;
import com.utils.JsonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Json
 * @date 2021/2/17 16:48
 */
@Service
public class ArticleMakerServiceImpl implements ArticleMakerService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleMakerServiceImpl.class);

    private final GridFSBucket gridFSBucket;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ArticleMakerServiceImpl(GridFSBucket gridFSBucket, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.gridFSBucket = gridFSBucket;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public void generate(String articleId) throws Exception {

        // 1.获得静态化html在gridFs中的主键
        String mongoFileId = createArticleHTMLToGridFS(articleId);

        // 2.存储到对应的文章 进行关联保存
        updateArticleToGridFS(articleId, mongoFileId);

        // 3.发送消息到MQ队列 让消费者监听并且执行下载html
        doDownloadArticleHTMLByMQ(articleId, mongoFileId);
    }

    @Override
    public void download(String articleId, String mongoFileId) throws Exception {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        OutputStream outputStream = null;
        try {
            // 3.创建输出流
            outputStream = new FileOutputStream(file);

            // 4.执行下载
            gridFSBucket.downloadToStream(new ObjectId(mongoFileId), outputStream);
        } finally {
            // 5.关闭输出流
            assert outputStream != null;
            outputStream.close();
        }
    }

    @Override
    public void delete(String articleId) {
        // 1.拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";

        // 2.获取文件流 定义存放的位置和名称
        File file = new File(path);

        // 3.删除文件
        file.delete();
    }

    // 文章生成HTML并存储到gridFs中
    private String createArticleHTMLToGridFS(String articleId) throws Exception {
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);

        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

        InputStream inputStream = IOUtils.toInputStream(htmlContent);
        ObjectId fileId = gridFSBucket.uploadFromStream(detailVO.getId() + ".html", inputStream);
        return fileId.toString();
    }

    /**
     * 发起远程调用rest 获得文章详情数据
     */
    private ArticleDetailVO getArticleDetail(String articleId) {
        String url = RoutingConstant.PORTAL_ARTICLE_DETAIL + "?articleId=" + articleId;
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(url, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult != null && bodyResult.getStatus() == HttpStatus.OK.value()) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
    }

    /**
     * 发起远程调用rest 进行关联保存
     */
    private void updateArticleToGridFS(String articleId, String mongoFileId) {
        String url = RoutingConstant.SAVE_ARTICLE_MONGO_FILEID + "?articleId=" + articleId + "&mongoFileId=" + mongoFileId;
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(url, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        if (bodyResult != null && bodyResult.getStatus() == HttpStatus.OK.value()) {
            logger.debug("articleId:{},mongoFileId:{},关联保存成功", articleId, mongoFileId);
        }
    }

    /**
     * 发送下载文章HTML的消息到MQ
     */
    private void doDownloadArticleHTMLByMQ(String articleId, String mongoFileId) {

        ArticleDownloadDTO downloadDTO = new ArticleDownloadDTO(articleId, mongoFileId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                RoutingKeyConstant.ARTICLE_DOWNLOAD_DO, JsonUtils.objectToJson(downloadDTO));
    }

}
