package com.zz.amqp1.service;

import com.zz.amqp1.bean.SMSMessage;
import com.zz.amqp1.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description: 用来发送短信的 service
 * User: zhouzhou
 * Date: 2019-03-02
 * Time: 9:39 AM
 */
@Service
public class SMSSendService {

    public static final Logger log = LoggerFactory.getLogger(SMSSendService.class);

    public final static String SMS_EXCHANGE = "message.direct";

    public final static String SMS_ROUTE_KEY = "sms";

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     * @param msg 消息内容
     * @return 消息体
     */
    public SMSMessage sendSMSMessage(String msg){
        SMSMessage smsMessage = new SMSMessage();
        String uuid = UUIDUtils.generateUUID();
        smsMessage.setSmsID(uuid);
        smsMessage.setDate(new Date());
        smsMessage.setMsg(msg);
        try {
            amqpTemplate.convertAndSend(SMS_EXCHANGE,SMS_ROUTE_KEY,smsMessage);
        } catch (Exception e) {
            log.warn(String.format("发送消息失败,消息内容为%s", msg));
            throw new RuntimeException(String.format("发送消息失败,消息内容为%s", msg));
        }

        return smsMessage;
    }

}
