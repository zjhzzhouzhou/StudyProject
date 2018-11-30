package com.zz.amqp1.bean;

import lombok.Data;

/**
 * Description: 玺号Model
 * User: zhouzhou
 * Date: 2018-09-17
 * Time: 19:15
 */
@Data
public class XhCodeIModel {

    // id
    private int id;
    // 电话区号
    private String telCode;
    // 玺号
    private String xhCode;
    // 玺号对应的计数器
    private int count;

}
