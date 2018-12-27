package com.zz.amqp1;

import com.zz.amqp.common.bean.BizSession;
import com.zz.amqp.common.model.Student;
import com.zz.amqp.common.utils.BizSessionUtils;

import java.util.Random;

/**
 * Description:ThreadLocal测试类, 线程共享变量
 * <p>
 * User: zhouzhou
 * Date: 2018-12-11
 * Time: 10:18 AM
 */
public class ThreadLocalTest {

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            // 创建两个线程, 测试每个线程的ThreadLocal是否独立, 数据是否安全
            new Thread(() -> {
                Student student = Student.builder()
                        .sex("男")
                        .age(new Random().nextInt(20) + 18)
                        .name("学生" + new Random().nextInt(5)).build();
                BizSessionUtils.setStudent(student);
                System.out.println(String.format("{%s}中存入对象为{%s}", getThreadName(), BizSession.currentSession()));
                System.out.println(getThreadName() + String.format("开始获取学生:{%s},获取环境{%s}", OrderHelper.getStudent(), OrderHelper.getEnv()));

                // 销毁session
                System.out.println(getThreadName() + "正在销毁当前会话");
                BizSession.destroy();
                System.out.println(getThreadName() + "销毁完毕, 尝试获取线程对象:" + BizSessionUtils.getBizContext());
                }, "线程" + (i + 1)).start();
        }
    }


    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * order helper support two functions:
     * 1. get student in current session
     * 2. get current system environment
     */
    static class OrderHelper {

        /**
         * get current Student in current Session
         *
         * @return Student
         */
        public static Student getStudent() {
            return BizSession.currentSession().getStudent();
        }

        /**
         * return current System Environment
         *
         * @return system environment
         */
        public static String getEnv() {
            return BizSession.currentSession().getSystemEnv();
        }
    }

}
