package com.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 *
 * @author Json
 * @date 2021/2/19 0:37
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 定义交换机的名字
     */
    public static final String EXCHANGE_ARTICLE = "exchange_article";

    /**
     * 定义队列的名字
     */
    public static final String QUEUE_ARTICLE_HTML = "queue_article_html";

    /**
     * 创建交换机
     */
    @Bean(EXCHANGE_ARTICLE)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_ARTICLE)
                .durable(true) // 开始持久化
                .build();
    }

    /**
     * 创建队列
     */
    @Bean(QUEUE_ARTICLE_HTML)
    public Queue queue() {
        return new Queue(QUEUE_ARTICLE_HTML);
    }

    /**
     * 队列绑定交换机
     */
    @Bean
    public Binding binding(@Qualifier(EXCHANGE_ARTICLE) Exchange exchange,
                           @Qualifier(QUEUE_ARTICLE_HTML) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("article.#.do")
                .noargs();  // 执行绑定
    }
}
