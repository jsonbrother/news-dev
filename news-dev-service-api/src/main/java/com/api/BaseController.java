package com.api;

import com.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by TongHaiJun
 * 2021/1/16 17:01
 */
public class BaseController {

    @Autowired
    protected RedisOperator redis;

    protected static final String MOBILE_SMSCODE = "mobile:smscode";
}
