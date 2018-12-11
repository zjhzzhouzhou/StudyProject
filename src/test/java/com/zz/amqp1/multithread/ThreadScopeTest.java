package com.zz.amqp1.multithread;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-11
 * Time: 9:48 AM
 */
public class ThreadScopeTest {
    //其实ThreadLocal本身就是一个map,key为thread
    private static Map<Thread, Object> threadObjectMap = Maps.newHashMap();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                int data = new Random().nextInt();
                threadObjectMap.put(Thread.currentThread(), data);
                System.out.println(String.format("{%s}的线程放入了:", Thread.currentThread().getName()) + data);
                System.out.println(String.format("获取{%s}中的变量为", Thread.currentThread().getName()) + ThreadUtils.getValue());
                System.out.println(String.format(String.format("摧毁线程{%s}中的变量", Thread.currentThread().getName())));
                ThreadUtils.destory();
                System.out.println(String.format("获取{%s}中的变量为", Thread.currentThread().getName()) + ThreadUtils.getValue());
            }, "线程" + (i + 1)).start();

        }


    }

    private static class ThreadUtils {
        public static Object getValue() {
            return threadObjectMap.get(Thread.currentThread());
        }

        public static void destory() {
            threadObjectMap.remove(Thread.currentThread());
        }
    }


}
