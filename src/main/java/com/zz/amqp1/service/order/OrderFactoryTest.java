package com.zz.amqp1.service.order;

import com.zz.amqp1.service.order.factory.AbstractOrderFactory;
import com.zz.amqp1.service.order.factory.ReserveOrderFactory;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 9:17 PM
 */
public class OrderFactoryTest {

    public static void main(String[] args) {
        AbstractOrderFactory orderFactory = new ReserveOrderFactory();
        IOrder order = orderFactory.createOrder();
        order.createOrder();
        order.cancelOrder();
    }
}
