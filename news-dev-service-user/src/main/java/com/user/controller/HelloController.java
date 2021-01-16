package com.user.controller;

import com.api.controller.user.HelloControllerApi;
import com.utils.RedisOperator;
import com.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TongHaiJun
 * 2021/1/15 11:28
 */
@RestController
public class HelloController implements HelloControllerApi {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    private final RedisOperator redis;

    @Autowired
    public HelloController(RedisOperator redis) {
        this.redis = redis;
    }

    public Object hello() {
        logger.info("info: hello~");
        return ResultUtil.success();
    }

    @Override
    public Object redis() {
        redis.set("name", "TongHaiJun");
        return ResultUtil.success(redis.get("name"));
    }
}
