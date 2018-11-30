package com.zz.amqp1.jvmtest;

import org.junit.Test;

/**
 * Description: -Xss128K 虚拟机栈设定为128k
 * User: zhouzhou
 * Date: 2018-11-08
 * Time: 15:31
 */
public class TestVMStack {
    private  static int count;

    // 作为循环
    public static void testLoop(){
        String value = "你好呀";

        count++;
        testLoop();
    }

    @Test
    public void test(){
        try {
            testLoop();
        } catch (Throwable e) {
            System.out.println(String.format("共创建了{%s}个栈帧", count));
            e.printStackTrace();
        }
    }
}
