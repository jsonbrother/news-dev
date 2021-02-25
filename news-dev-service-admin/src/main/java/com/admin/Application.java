package com.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Json
 * @date 2021/1/24 15:40
 */
@SpringBootApplication
@MapperScan(basePackages = "com.admin.mapper")
@ComponentScan(basePackages = {"com", "org.n3r.idworker"})
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
