package com.user.comtroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TongHaiJun
 * 2021/1/15 11:28
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "hello";
    }

}
