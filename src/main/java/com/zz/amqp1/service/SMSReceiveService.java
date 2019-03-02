package com.zz.amqp1.service;

import com.zz.amqp1.bean.SMSMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Description: 短消息接收
 * User: zhouzhou
 * Date: 2019-03-02
 * Time: 10:05 AM
 */
@Service
public class SMSReceiveService {

    public static final Logger log = LoggerFactory.getLogger(SMSSendService.class);


    @RabbitListener(queues = "sms")
    public void getMessage(SMSMessage smsMessage){
        log.info(String.format("接受到消息,消息体为{%s}", smsMessage));
    }
}
