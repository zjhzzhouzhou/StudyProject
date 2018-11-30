package com.zz.amqp1.service;

import com.zz.amqp1.bean.MqModel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-08-27
 * Time: 18:39
 */
@Service
@RabbitListener(queues = {"${mq.exchange.queue}"})
public class MessageRecieveService {

    @RabbitHandler
    public void recieveMqModelMessage(MqModel model) {
        System.out.println("保存数据:" + model);
    }


    @RabbitHandler
    public void recieveStringMessage(String msg) {

        System.out.println("========================接收文本消息成功==========================");
        System.out.println(msg);
    }


}
