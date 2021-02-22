package com.maker.consumer;

import com.api.config.RabbitMQConfig;
import com.constant.RoutingKeyConstant;
import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.maker.service.ArticleMakerService;
import com.pojo.dto.ArticleDownloadDTO;
import com.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Json
 * @date 2021/2/19 22:40
 */
@Component
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final ArticleMakerService articleMakerService;

    @Autowired
    public RabbitMQConsumer(ArticleMakerService articleMakerService) {
        this.articleMakerService = articleMakerService;
    }

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_ARTICLE_HTML})
    public void watchQueue(String payload, Message message) {
        logger.info("payload:{}", payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (routingKey.equalsIgnoreCase(RoutingKeyConstant.ARTICLE_GENERATE_DO)) {

            try {
                articleMakerService.generate(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (routingKey.equalsIgnoreCase(RoutingKeyConstant.ARTICLE_DOWNLOAD_DO)) {

            ArticleDownloadDTO downloadDTO = JsonUtils.jsonToPojo(payload, ArticleDownloadDTO.class);
            try {
                assert downloadDTO != null;
                articleMakerService.download(downloadDTO.getArticleId(), downloadDTO.getMongoFileId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (routingKey.equalsIgnoreCase(RoutingKeyConstant.ARTICLE_DELETE_DO)) {

            articleMakerService.delete(payload);
        } else {
            logger.info("不符合的规则:{}", routingKey);
        }
    }

}
