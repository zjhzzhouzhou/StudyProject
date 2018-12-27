package com.zz.amqp.test;

import com.zz.amqp.controller.StudentController;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-25
 * Time: 10:57
 */
public class ClassTest {

    @Test
    public void test1(){
        Class<? extends ClassTest> aClass = this.getClass();
        System.out.println(aClass.getName());
        System.out.println(aClass.getSimpleName());

        Field[] declaredFields = StudentController.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getType().getSimpleName());
        }
    }
}
