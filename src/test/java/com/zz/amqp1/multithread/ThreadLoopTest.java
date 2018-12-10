package com.zz.amqp1.multithread;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-10
 * Time: 2:06 PM
 */
public class ThreadLoopTest {

    // 一共要循环50次
    private static final int count = 8;

    private static volatile boolean flag = false;

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {


        //这个是主线程
        Thread main = new Thread(() -> {
            try {
                doMainTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "主线程");
        Thread son = new Thread(() -> {
            try {
                doSubTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "子线程");

        main.start();
        son.start();


    }

    private static void doSubTask() throws Exception {
        // 循环10次
        for (int i = 0; i < 10; i++) {
            synchronized (lock){
                while(!flag){
                    lock.wait();
                }
                MissionUtils.subMission();
                flag = false;
                lock.notify();
            }
        }

    }

    private static void doMainTask() throws Exception{
        // 循环10次
        for (int i = 0; i < 10; i++) {
            synchronized (lock){
                while(flag){ //condition does not hold
                    lock.wait();
                }
                MissionUtils.mianMission();
                flag = true;
                lock.notify();


            }

        }

    }


    private static class MissionUtils {

        public static void mianMission() {
            for (int i = 0; i < 5; i++) {
                System.out.println(String.format("主线程第{%s}次", i + 1));
            }
        }

        public static void subMission() {
            for (int i = 0; i < 8; i++) {
                System.out.println(String.format("子线程第{%s}次", i + 1));
            }
        }
    }

}
