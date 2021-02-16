package com.article.controller;

import com.api.controller.user.HelloControllerApi;
import com.result.NewsJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Json
 * @date 2021/2/6 21:50
 */
@RestController
public class HelloController implements HelloControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Override
    public Object hello() {
        logger.info("info: hello~");
        return NewsJSONResult.success();
    }

}
