package com.zz.amqp1.multithread;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-05
 * Time: 15:56
 */
public class DoubleCheckSingleton {

    private static DoubleCheckSingleton ds;

    public static DoubleCheckSingleton getSingleton() {
        if (ds == null) {
            synchronized (DoubleCheckSingleton.class) {
                if (ds == null) {
                    try {
                        // 初始化单例需要3秒
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ds = new DoubleCheckSingleton();
                }
            }
        }
        return ds;
    }

    public static void main(String[] args) {
        new Thread(() -> System.out.println(DoubleCheckSingleton.getSingleton().hashCode())).start();
        new Thread(() -> System.out.println(DoubleCheckSingleton.getSingleton().hashCode())).start();
        new Thread(() -> System.out.println(DoubleCheckSingleton.getSingleton().hashCode())).start();
    }
}
