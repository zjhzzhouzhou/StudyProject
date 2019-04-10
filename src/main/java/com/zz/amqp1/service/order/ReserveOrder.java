package com.zz.amqp1.service.order;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 9:02 PM
 */
public class ReserveOrder implements IOrder {
    @Override
    public void createOrder() {
        System.out.println("生成预约订单");
    }

    @Override
    public void cancelOrder() {
        System.out.println("取消预约订单");
    }
}
