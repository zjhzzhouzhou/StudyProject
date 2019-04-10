package com.zz.amqp1.service.order.factory;

import com.zz.amqp1.service.order.IOrder;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 9:13 PM
 */
public abstract class AbstractOrderFactory {

    public abstract IOrder createOrder();

    public void preHandle(){
        System.out.println("正在进行预处理");
    }
}
