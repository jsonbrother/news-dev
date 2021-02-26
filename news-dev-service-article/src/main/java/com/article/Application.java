package com.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Json
 * @date 2021/2/6 21:46
 */
@SpringBootApplication()
@MapperScan(basePackages = "com.article.mapper")
@ComponentScan(basePackages = {"com", "org.n3r.idworker"})
@EnableEurekaClient
@EnableFeignClients({"com"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
