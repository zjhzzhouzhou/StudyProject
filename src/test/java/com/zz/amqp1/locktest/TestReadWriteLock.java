package com.zz.amqp1.locktest;

import lombok.Data;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description:读写锁
 * <p>
 *     1 写写, 读写, 需要互斥
 *     2 读读, 不需要互斥
 *
 * </p>
 * User: zhouzhou
 * Date: 2018-12-27
 * Time: 2:20 PM
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                demo.set(new Random().nextInt(101));
            },"write:" + (i+1)).start();
        }


        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                demo.read();
            }).start();
        }
    }
}

@Data
class ReadWriteLockDemo{

    private  int number = 0;

    // 可重入读写锁
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void read(){
        lock.readLock().lock();
        try {
            System.out.println("当前的值为"  + this.number);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(Integer value){
        lock.writeLock().lock();
        try {
            System.out.println(String.format("当前线程{%s}正在进行写操作{%s}", Thread.currentThread().getName(),value));
            this.setNumber(value);
        } finally {
            lock.writeLock().unlock();
        }

    }

}