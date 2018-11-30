package com.zz.amqp1.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * Description: 异步任务测试类
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 13:30
 */
@Service
public class AsyncService {

    @Async
    public void doNoReturn(){
        try {
            // 这个方法执行需要三秒
            Thread.sleep(3000);
            System.out.println("方法执行结束" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Async
    public Future<String> doReturn(int i){
        try {
            // 这个方法需要调用500毫秒
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 消息汇总
        return new AsyncResult<>(String.format("这个是第{%s}个异步调用的证书", i));
    }
}
