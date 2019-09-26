package com.zz.amqp1.responsibility;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-09-04
 * Time: 9:38
 */
public interface IRequestProcessor {

    void process(ZRequest request);

}
