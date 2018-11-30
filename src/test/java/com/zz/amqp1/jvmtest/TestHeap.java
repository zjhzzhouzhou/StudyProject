package com.zz.amqp1.jvmtest;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:虚拟机栈, 本地方法溢出:-Xss 256k
 * 方法区和运行时常量的溢出: -Xms 5M -Xmx 5M -XX:+PrintGCDetails -verbose:gc
 * 本机直接内存溢出: -Xmx10M, -XX:MaxDirectMemorySize=10M
 * 方法区和运行时常量溢出:  -XX: MaxMetaspaceSize=3M
 * User: zhouzhou
 * Date: 2018-11-07
 * Time: 16:13
 */
public class TestHeap {
    public static void main(String[] args) {
        List<Object> heapList = new ArrayList<>();
        int i = 0;
        while(true){
            i++;
            if (i%1000 == 0){
                System.out.println(String.format("已插入对象:{%s}个",i ));
            }
            // 一般堆内存默认512MB
            //一个new Object() 不加任何参数一般是几kb
            heapList.add(new Object());
        }
    }


}
