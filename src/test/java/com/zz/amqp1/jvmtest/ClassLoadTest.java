package com.zz.amqp1.jvmtest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-21
 * Time: 15:36
 */


public class ClassLoadTest {

    public final static String className = "com.zz.amqp1.controller.AsyncController";

    @Test
    public void test() throws Exception {
        Class<?> clazz = Class.forName(className);
        String clazzName = clazz.getSimpleName();
        Object obj = clazz.newInstance();
        // 填充对象中的@autowird的类型
        fillObject(obj);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name = method.getName();
            System.out.println("方法名为: " + name);
            System.out.println(String.format("执行方法:{%s}", name));
            Class<?> returnTypeClazz = method.getReturnType();
            Object invoke = method.invoke(obj);
            System.out.println("当前返回类型为:" + returnTypeClazz.getSimpleName());
            System.out.println("返回结果为:" + invoke);


        }
        System.out.println("当前类名" + lowFirstChar(clazzName));
        System.out.println("当前对象" + obj);
    }

    private void fillObject(Object obj) throws Exception {
        // 获取所有的字段无论是否私有
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 不是所有的牛奶都是特仑苏
            if (field.isAnnotationPresent(Autowired.class)) {
                Object o = field.getType().newInstance();
                field.setAccessible(true);// 强上了
                field.set(obj,o);
            }
        }
    }

    private String lowFirstChar(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
