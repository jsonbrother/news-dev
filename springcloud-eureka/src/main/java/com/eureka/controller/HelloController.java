package com.eureka.controller;

import com.result.NewsJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public NewsJSONResult hello() {
        return NewsJSONResult.success();
    }

}
