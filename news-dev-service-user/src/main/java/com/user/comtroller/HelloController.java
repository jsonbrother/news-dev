package com.user.comtroller;

import com.api.comtroller.user.HelloControllerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TongHaiJun
 * 2021/1/15 11:28
 */
@RestController
public class HelloController implements HelloControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello() {

        logger.debug("debug: hello~");
        logger.info("info: hello~");
        logger.warn("warn: hello~");
        logger.error("error: hello~");

        return "hello";
    }

}
