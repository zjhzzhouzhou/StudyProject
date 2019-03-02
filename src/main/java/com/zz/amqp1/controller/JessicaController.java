package com.zz.amqp1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 异步任务测试类
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 13:33
 */
@RestController
public class JessicaController {


    @RequestMapping(value = "/hello/Jessica", method = RequestMethod.GET)
    public String testHello() {
        return "爱你。Jessica";
    }

    @RequestMapping(value = "/hello/Neal", method = RequestMethod.GET)
    public String testHelloNeal() {
        return "hello。Neal";
    }

}
