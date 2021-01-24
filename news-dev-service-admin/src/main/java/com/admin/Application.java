package com.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by TongHaiJun
 * 2021/1/24 15:40
 */
@SpringBootApplication
@MapperScan(basePackages = "com.admin.mapper")
@ComponentScan(basePackages = {"com", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
