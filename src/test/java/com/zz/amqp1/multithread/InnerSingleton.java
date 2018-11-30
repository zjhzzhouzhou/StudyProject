package com.zz.amqp1.multithread;

/**
 * Description: 最简单好用的单例
 * User: zhouzhou
 * Date: 2018-11-05
 * Time: 15:50
 */
public class InnerSingleton {

    private static class SingletonInstance {
        private static  SingletonInstance singletonInstance = new SingletonInstance();
    }

    private SingletonInstance getSingleton(){
        return  SingletonInstance.singletonInstance;
    }
}
