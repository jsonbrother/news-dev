package com.api.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by TongHaiJun
 * 2021/1/15 18:11
 */

/**
 * swagger2配置
 *
 * @author Json
 * @date 2021/1/15 18:11
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     * 配置swagger2核心配置 docket
     * http://localhost:8088/swagger-ui.html 原路径
     * http://localhost:8088/doc.html 新路径
     */
    @Bean
    public Docket createRestApi() {

        Predicate<RequestHandler> userPredicate = RequestHandlerSelectors.basePackage("com.user.controller");
        Predicate<RequestHandler> filesPredicate = RequestHandlerSelectors.basePackage("com.files.controller");
        Predicate<RequestHandler> adminPredicate = RequestHandlerSelectors.basePackage("com.admin.controller");

        // 指定api类型为swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                // 用于定义api文档汇总信息
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(userPredicate, filesPredicate, adminPredicate))
                // 所有controller
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档页标题
                .title("今日新闻·自媒体接口api")
                // 联系人信息
                .contact(new Contact("today", "https://www.today.com", "abc@today.com"))
                // 详细信息
                .description("专为今日新闻·自媒体平台提供的api文档")
                // 文档版本号
                .version("1.0.1")
                // 网站地址
                .termsOfServiceUrl("https://www.today.com")
                .build();
    }
}
