package com.zz.amqp1.controller;

import com.zz.amqp1.common.redistoken.RateLimitClient;
import com.zz.amqp1.common.redistoken.RateLimiterParam;
import com.zz.amqp1.common.redistoken.Token;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-14
 * Time: 5:22 PM
 */
@RestController
public class RateLimiterController {

    @Autowired
    RateLimitClient rateLimitClient;

    @GetMapping(value = "/getToken")
    @ResponseBody
    public String getToken() {
        //ratelimit:skynet
        Boolean token = rateLimitClient.tryAcquire("skynet");
        if (token) {
            return "success";
        } else {
            return "failed";
        }
    }



    @GetMapping(value = "/initToken")
    @ResponseBody
    public String initToken() {

        RateLimiterParam rateLimiterParam = RateLimiterParam.builder().app("skynet")
                .maxPermits(50)
                .name("skynet")
                .rate(100).build();

        //ratelimit:skynet
        Token token = rateLimitClient.initToken(rateLimiterParam);
        if (token.isSuccess()) {
            return "success";
        } else {
            return "failed";
        }
    }

    @GetMapping(value = "/deleteToken")
    @ResponseBody
    public String deleteToken() {
        //ratelimit:skynet
        Boolean token = rateLimitClient.deleteRateLimiter("skynet");
        if (token) {
            return "success";
        } else {
            return "failed";
        }
    }


}
