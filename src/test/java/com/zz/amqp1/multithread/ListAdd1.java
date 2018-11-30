package com.zz.amqp1.multithread;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-02
 * Time: 17:04
 */
public class ListAdd1 {
    private volatile static List list = new ArrayList();

    public void add(){
        list.add("我真是是要爆炸了");
    }
    public int size(){
        return list.size();
    }

    public static void main(String[] args) {
        final ListAdd1 add1 = new ListAdd1();

        final Object lock = new Object();
        Thread thread1 = new Thread(()->{
            try {
                synchronized (lock){
                    for (int i = 0; i < 10; i++) {
                        add1.add();
                        System.out.println(String.format("当前线程{%s}添加了一个元素", Thread.currentThread().getName()));
                        Thread.sleep(500);
                        if (list.size() == 5){
                            System.out.println("发出通知");
                            lock.notify();
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("发生异常了");
            }
        },"t1");

        Thread thread2 = new Thread(()->{
            synchronized (lock){
                if (add1.size() != 5){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(String.format("当前线程{%s}收到通知,线程停止", Thread.currentThread().getName()));
                throw  new RuntimeException();
            }
        },"t2");
        thread2.start();
        thread1.start();
    }
}
