package com.article.controller;

import com.api.config.RabbitMQConfig;
import com.result.NewsJSONResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Json
 * @date 2021/2/19 22:33
 */
@RestController
@RequestMapping("/producer")
public class HelloController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public HelloController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/hello")
    public Object hello() {
        /*
         * RabbitMQ 的路由规则 routing key
         *  display.*.* -> * 代表一个占位符
         *      例：
         *          display.do.download  匹配
         *          display.do.upload.done   不匹配
         *
         * display.# -> # 代表任意多个占位符
         *      例:
         *          display.do.download  匹配
         *          display.do.upload.done.over   匹配
         *
         *
         */
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.publish.download.do",
                "1001");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.success.do",
                "1002");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.error.do",
                "1002");

        return NewsJSONResult.success();
    }
}
