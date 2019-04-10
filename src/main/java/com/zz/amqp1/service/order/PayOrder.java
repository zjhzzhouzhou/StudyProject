package com.zz.amqp1.service.order;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 9:02 PM
 */
public class PayOrder implements IOrder {
    @Override
    public void createOrder() {
        System.out.println("生成交易订单");
    }

    @Override
    public void cancelOrder() {
        System.out.println("取消交易订单");
    }
}
