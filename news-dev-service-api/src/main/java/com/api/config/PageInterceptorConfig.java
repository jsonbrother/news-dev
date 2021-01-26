package com.api.config;

import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分页插件配置
 * <p>
 * Created by TongHaiJun
 * 2021/1/26 10:53
 */
@EnableConfigurationProperties(PageHelperProperties.class)
@Configuration
public class PageInterceptorConfig {

    private final PageHelperProperties properties;

    @Autowired
    public PageInterceptorConfig(PageHelperProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        properties.setHelperDialect("mysql");
        properties.setSupportMethodsArguments("true");
        pageInterceptor.setProperties(properties.getProperties());
        return pageInterceptor;
    }
}
