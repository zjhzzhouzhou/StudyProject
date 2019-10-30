package com.zz.amqp1.multithread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-09-29
 * Time: 16:42
 */
public class CountDownLatchDemo {

    @Test
    public void test1() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            System.out.println("Thead1");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("Thead2");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println("Thead3");
            countDownLatch.countDown();
        }).start();

        TimeUnit.SECONDS.sleep(1);
        countDownLatch.await();
        System.out.println("over");
    }

    @Test
    public void test2() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);


        for (int i = 0; i < 55; i++) {
            Integer count = i;
            new Thread(() -> {
                try {
                    countDownLatch.await();


                } catch (InterruptedException e) {

                }
                System.out.println(String.format("加载缓存%s号", count));



            }).start();
        }
        countDownLatch.countDown();




    }

}
