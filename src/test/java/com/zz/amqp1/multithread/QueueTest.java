package com.zz.amqp1.multithread;

import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-16
 * Time: 15:00
 */
public class QueueTest {

    @Test // 测试无阻塞无界队列
    public void testConcurrentLinkQueue(){
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("zhouzhou1");
        queue.offer("zhouzhou2");
        queue.offer("zhouzhou3");
        queue.offer("zhouzhou4");

        System.out.println(queue.peek());
        System.out.println(queue.size());
        System.out.println(queue.poll());
        System.out.println(queue.size());
    }

    @Test
    public void test02(){
        Object obj = 2;
        System.out.println(obj instanceof String);
        System.out.println(String.class.isInstance(obj));
    }
}
