package com.maker.consumer;

import com.api.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Json
 * @date 2021/2/19 22:40
 */
@Component
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_DOWNLOAD_HTML})
    public void watchQueue(String payload, Message message) {
        logger.info(payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (routingKey.equalsIgnoreCase("article.publish.download.do")) {
            logger.info("article.publish.download.do");
        } else if (routingKey.equalsIgnoreCase("article.success.do")) {
            logger.info("article.success.do");
        } else {
            logger.info("不符合的规则:{}", routingKey);
        }
    }

}
