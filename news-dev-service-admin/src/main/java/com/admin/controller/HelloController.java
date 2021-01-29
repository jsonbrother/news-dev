package com.admin.controller;

import com.api.controller.user.HelloControllerApi;
import com.result.NewsJSONResult;
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

    @Override
    public Object hello() {
        logger.info("info: hello~");
        return NewsJSONResult.success();
    }

    @Override
    public Object redis() {
        return null;
    }

//    public static void main(String[] args) {
//
//        String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());
//        System.out.println(pwd);
//
//    }

}
