package com.zz.amqp1.controller;

import com.zz.amqp1.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Description: 异步任务测试类
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 13:33
 */
@RestController
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String testAsyncNoRetrun() {
        long start = System.currentTimeMillis();
        asyncService.doNoReturn();
        return String.format("任务执行成功,耗时{%s}", System.currentTimeMillis() - start);
    }

    @GetMapping("/hi")
    public Map<String, Object> testAsyncReturn() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        List<String> response = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<String> future = asyncService.doReturn(i);
            response.add(future.get());
        }

        map.put("data", response);
        map.put("消耗时间", String.format("任务执行成功,耗时{%s}毫秒", System.currentTimeMillis() - start));
        return map;
    }
}
