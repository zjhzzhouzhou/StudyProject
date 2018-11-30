package com.zz.amqp1;

import com.zz.amqp1.bean.Student;
import com.zz.amqp1.utils.ValidateUtil;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-05
 * Time: 8:59
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NormalTest {

    @Test
    public void test33(){
        String str = "330104";
        System.out.println(ValidateUtil.isValidInt(str));
    }

    @Test
    public void test1() {
        String str = "fasdfasfasd/v123456.bks";
        System.out.println(str.substring(0, str.indexOf(".")).substring(str.indexOf("/") + 1));
    }

    @Test
    public void test5() {
        String str = "(3)";
        System.out.println(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
    }

    @Test
    public void test11() {
        String str = "91310105132218087U";
        System.out.println(str.substring(2,8));
    }

    @Test
    public void test12() {
        String str = "小明";
        Student student = new Student();
        student.setName(str);
        str = "小红";
        System.out.println(student);
        System.out.println(str);
    }

    @Test
    public void test13() {
        long num1 = 123l;
        System.out.println((num1 + 1)+"");
    }

    @Test
    public void test2() {
        System.out.println(2 << 11);
    }

    @Test
    public void test03() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 30; i++) {
            scheduledExecutorService.schedule(()-> {
                System.out.println("娃哈哈");
                    return null;
            }, 3, TimeUnit.SECONDS);
        }
        scheduledExecutorService.shutdown();

    }

    @Test
    public void test04() {
        String str = "0571";
        int count = 129;
        System.out.println(str + String.format("%08d", count));
    }

    @Test
    public void test055(){
        String str = "String";
        char[] chars = str.toCharArray();
        chars[0] += 32;
        System.out.println(String.valueOf(chars));
    }

    @Test
    public void test051(){
        String str = "/String///testtest.json";

        System.out.println(str.replaceAll("/+","/"));
    }

    @Test
    public void test05() {
        ExecutorService pool = Executors.newCachedThreadPool();
        for ( int i = 0; i < 3; i++) {
            pool.execute(() -> {
                System.out.println("执行线程");
                printNum();
            });
        }
        pool.isShutdown();
    }

    @Test
    public void test06() throws  Throwable{
        System.out.println("开始了");
        long a = 0;
        if (a == 0){
            a = 3000;
        }
        Thread.sleep(a);
        System.out.println(String.format("沉睡了{%s}毫秒", a));
    }

    public void printNum() {
        int num = 1;
        for (int i = 1; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + String.format("打印数字开始第%d次,数字是%d", i, num));
            num++;
        }
    }

    @Test
    public void testString(){
        String value = "organizationName";
        String method = "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
        System.out.println(method);
    }

    @Test
    public void testCompare(){
        List<String> list = Lists.newArrayList("12","13","11","22","21","31","41");
        Collections.sort(list, (String s1, String s2) ->(s1.compareTo(s2)));
        System.out.println(list);
    }

    @Test
    public void testList(){
        List<Integer> list = Lists.newArrayList(1,56,1,85,56,16,1,3,84,1,65,111);
        List<Integer> collect = list.stream().filter(i -> i > 30).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test41789204(){
        String str = "12345678901234567890";
        String substring = str.substring(0, 18);
        System.out.println(substring);
    }


}
