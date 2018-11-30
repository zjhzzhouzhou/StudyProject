package com.zz.amqp1.bean;

import lombok.Data;

/**
 * Description: 玺号搜索条件
 * User: zhouzhou
 * Date: 2018-09-26
 * Time: 13:18
 */
@Data
public class XhCodeSearchIModel {

    // 电话区号
    private String telCode;
    // 玺号
    private String xhCode;
    // 玺号对应的计数器
    private int count;


}
