package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by TongHaiJun
 * 2021/1/15 11:33
 */
@SpringBootApplication()
@MapperScan(basePackages = "com.user.mapper")
@ComponentScan(basePackages = {"com", "org.n3r.idworker"})
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
