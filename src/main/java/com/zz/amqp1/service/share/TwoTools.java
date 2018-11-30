package com.zz.amqp1.service.share;

import org.springframework.stereotype.Component;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-07
 * Time: 10:30
 */
@Component
public class TwoTools implements ShareHandler{

    @Override
    public void print() {
        System.out.println("这是二号工具");
        System.out.println(Thread.currentThread().getName() + "二号工具开始操作");
    }

}
