package com.zz.amqp1.bean;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-12
 * Time: 10:59
 */
public class StudentThrealLocal  {
    public static final   ThreadLocal<Student> studentThreadLocal = new ThreadLocal<Student>(){
        @Override
        protected Student initialValue() {
            System.out.println("随便初始化一个值");
            return null;
        }
    };
}
