package com.zz.amqp1.multithread;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 利用wait notify 实现
 * User: zhouzhou
 * Date: 2018-11-05
 * Time: 14:22
 */
public class MyQueue {
    // 1 需要一个承装元素的集合
    private final LinkedList<Object> list = new LinkedList<>();

    // 2 需要一个计数器
    private AtomicInteger count = new AtomicInteger(0);

    // 3. 需要制定上限, 和下限
    private final int minSize = 0;
    private final int maxSize ;

    // 构造方法
    public  MyQueue(int size){
        this.maxSize = size;
    }

    //初始化对象用于加锁
    private final Object lock = new Object();

    //put 将一个Object加到BlockingQueue里, 如果BlockingQueue没有空间, 此方法的线程被截断,知道BlockingQueue有空间
    public void put (Object object) {
        synchronized (lock){
            while(this.maxSize == count.get()){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 加入元素
            list.add(object);
            // 计数器累加
            count.incrementAndGet();
            System.out.println(String.format("新加入的元素为:{%s}", object));
            // 通知另外一个线程进行唤醒操作
            lock.notify();
        }
    }

    // take: 取走BlockingQueue里排在首位的对象, 若BlockingQueue为空,阻断等待线程知道BlockingQueue中有新的数据加进来位置
    public Object take(){
        Object ret ;
        synchronized (lock){
            while(count.get() == this.minSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 1 做移除元素的操作
            ret = list.removeFirst();
            // 计数器递减
            count.decrementAndGet();
            // 唤醒另外一个线程
            lock.notify();

        }
        return ret;
    }


    public int getSize(){
        return this.count.get();
    }


    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue(5);
        myQueue.put("1");
        myQueue.put("2");
        myQueue.put("3");
        myQueue.put("4");
        myQueue.put("5");
        System.out.println(String.format("当前容器长度为{%s}", myQueue.getSize()));
        Thread t1 = new Thread(()->{
            myQueue.put("6");
            myQueue.put("7");
        },"t1");
        t1.start();

        Thread t2 = new Thread(()->{
            Object o1 = myQueue.take();
            System.out.println(String.format("移除的元素为{%s}", o1));
            Object o2 = myQueue.take();
            System.out.println(String.format("移除的元素为{%s}", o2));
        },"t2");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
    }

}
