package com.zz.amqp1.controller;

import com.zz.amqp1.bean.SMSMessage;
import com.zz.amqp1.bean.Student;
import com.zz.amqp1.config.Response;
import com.zz.amqp1.service.MessageSendService;
import com.zz.amqp1.service.SMSSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 15:39
 */
@RestController
public class MessageController {

    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private SMSSendService smsSendService;

    @GetMapping("/sendstr")
    public String sendStrMessage() {
        String msg = String.format("发送一条文字消息,幸运数字为:{%s}", new Random().nextInt(100));
        messageSendService.sendStringMessage(msg);
        System.out.println(String.format("消息文本发送成功:{%s}", msg));
        return "消息文本发送成功" + msg;
    }

    @GetMapping("/sendstu")
    public String sendStuMessage() {
        Student student = new Student();
        student.setName("周舟");
        student.setSex("男");
        student.setAge(new Random().nextInt(100));
        messageSendService.sendStringMessage(student);
        return "消息实体发送成功" + System.currentTimeMillis();
    }

    @GetMapping("/sendSms/{msg}")
    public Response<SMSMessage> sendSmsMessage(@PathVariable(value = "msg") String msg){
        Response<SMSMessage> response = new Response<>();
        SMSMessage smsMessage = smsSendService.sendSMSMessage(msg);
        response.setMsg("消息发送成功");
        response.setCode(Response.HttpResponseStatus.OK);
        response.setData(smsMessage);
        return response;
    }

}
