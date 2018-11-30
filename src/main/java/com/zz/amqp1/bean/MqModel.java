package com.zz.amqp1.bean;

import lombok.Data;

/**
 * Description:Mq 专属model
 * User: zhouzhou
 * Date: 2018-09-21
 * Time: 18:52
 */
@Data
public class MqModel<T> {

    private String mqNo;
    /** 响应数据 */
    private T data;
    /** 响应状态 */
    private Boolean status = true;
    /** 响应消息 */
    private String msg;

}
