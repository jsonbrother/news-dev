package com.user.comtroller;

import com.api.comtroller.user.HelloControllerApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TongHaiJun
 * 2021/1/15 11:28
 */
@RestController
public class HelloController implements HelloControllerApi {

    public Object hello() {
        return "hello";
    }

}
