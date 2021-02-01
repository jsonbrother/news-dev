package com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by TongHaiJun
 * 2021/1/31 17:43
 */
@Configuration
public class CloudConfig {

    public CloudConfig() {

    }

    /**
     * 基于OKHttp3的配置来配置RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
