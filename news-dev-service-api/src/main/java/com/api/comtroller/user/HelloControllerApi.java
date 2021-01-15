package com.api.comtroller.user;


import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by TongHaiJun
 * 2021/1/15 12:32
 */
public interface HelloControllerApi {

    @GetMapping("/hello")
    Object hello();

}
