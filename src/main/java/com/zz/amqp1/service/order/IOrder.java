package com.zz.amqp1.service.order;

/**
 * Description: 订单
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 8:35 PM
 */
public interface IOrder {

    /**
     * 生成订单
     * @return 订单
     */
    void createOrder();

    /**
     * 取消订单
     * @return 结果
     */
    void cancelOrder();

}
