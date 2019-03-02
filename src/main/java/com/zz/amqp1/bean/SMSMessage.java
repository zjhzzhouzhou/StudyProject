package com.zz.amqp1.bean;

import lombok.Data;

import java.util.Date;

/**
 * Description: sms信息
 * User: zhouzhou
 * Date: 2019-03-02
 * Time: 9:46 AM
 */
@Data
public class SMSMessage {
    /**
     * 消息 id
     */
    private String smsID;

    /**
     * 消息时间
     */
    private Date date;

    /**
     * 消息体
     */
    private String msg;

}
