package com.zz.amqp1.jvmtest;

import com.zz.amqp1.bean.Student;
import com.zz.amqp1.bean.StudentThrealLocal;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-12
 * Time: 10:30
 */
public class ThreadLocalTest {

    private static ThreadLocal<Student> threadLocal = StudentThrealLocal.studentThreadLocal;

    public static void main(String[] args) {
        Student student = threadLocal.get();
        System.out.println("获取初始化的threadLocal变量值" + student);
        student.setName("周舟假的");
        threadLocal.set(student);
        System.out.println("修改后的threadLoacl变量" + threadLocal.get());
        threadLocal.remove();
        System.out.println("清空后的threadLocal变量" + threadLocal.get());

    }


}
