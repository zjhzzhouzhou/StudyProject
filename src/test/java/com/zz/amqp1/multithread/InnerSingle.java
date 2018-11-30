package com.zz.amqp1.multithread;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-12
 * Time: 11:33
 */
public class InnerSingle {

    private static class InnerSingleInstance{
        private static InnerSingleInstance innerSingleInstance= new InnerSingleInstance();
    }

    private InnerSingleInstance getInstance(){
        return InnerSingleInstance.innerSingleInstance;
    }
}
