package com.article.consumer;

import com.api.config.RabbitMQDelayConfig;
import com.article.service.ArticleService;
import com.constant.RoutingKeyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Json
 * @date 2021/2/21 17:30
 */
@Component
public class RabbitMQDelayConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQDelayConsumer.class);

    private final ArticleService articleService;

    @Autowired
    public RabbitMQDelayConsumer(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RabbitListener(queues = {RabbitMQDelayConfig.QUEUE_DELAY})
    public void watchQueue(String payload, Message message) {
        logger.info("payload:{}", payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (routingKey.equalsIgnoreCase(RoutingKeyConstant.PUBLISH_DELAY_DISPLAY)) {
            // 消费者接收到定时发布的延迟消息 修改当前的文章状态为 即时发布
            articleService.updateArticleToPublish(payload);
        } else {
            logger.info("不符合的规则:{}", routingKey);
        }
    }
}
