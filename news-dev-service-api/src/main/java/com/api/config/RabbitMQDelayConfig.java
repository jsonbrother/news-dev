package com.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 延迟消息队列配置
 *
 * @author Json
 * @date 2021/2/21 17:12
 */
@Configuration
public class RabbitMQDelayConfig {

    /**
     * 定义交换机的名字
     */
    public static final String EXCHANGE_DELAY = "exchange_delay";

    /**
     * 定义队列的名字
     */
    public static final String QUEUE_DELAY = "queue_delay";

    /**
     * 创建交换机
     */
    @Bean(EXCHANGE_DELAY)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_DELAY)
                .delayed() // 支持消息延迟
                .durable(true) // 开始持久化
                .build();
    }

    /**
     * 创建队列
     */
    @Bean(QUEUE_DELAY)
    public Queue queue() {
        return new Queue(QUEUE_DELAY);
    }

    /**
     * 队列绑定交换机
     */
    @Bean
    public Binding delayBinding(@Qualifier(EXCHANGE_DELAY) Exchange exchange, @Qualifier(QUEUE_DELAY) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("publish.delay.#")
                .noargs();      // 执行绑定
    }

}
