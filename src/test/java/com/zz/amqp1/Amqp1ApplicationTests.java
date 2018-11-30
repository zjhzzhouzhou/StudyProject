package com.zz.amqp1;

import com.zz.amqp1.bean.Student;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Amqp1ApplicationTests {
    public static char[] password = "changeit".toCharArray();

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {

        // Message需要自己构造Message ,消息体和消息头
        //rabbitTemplate.send(exchange,routingKey,message);

        //只需要传入要发送的对象,自动序列化发送给rabbitMq,object默认当成消息体
        //rabbitTemplate.convertAndSend(exchange,routingKey,object);
        Student student = new Student();
        student.setAge(187);
        student.setName("zz");
        student.setSex("boy");
        rabbitTemplate.convertAndSend("exchange.direct", "zzTest.news", student);
    }

    @Test
    public void recieve() {
        Student student = (Student) rabbitTemplate.receiveAndConvert("zzTest.news");
        System.out.println(student);
    }

    @Test
    public void sendFanout() {
        Student student = new Student();
        student.setName("周舟");
        student.setSex("男");
        student.setAge(18);
        rabbitTemplate.convertAndSend("exchange.fanout", "", student);
    }

    @Test
    public void testRandom() {

        System.out.println(new Random().nextInt(100));
    }

    @Test
    public void testDate() {
        String emptyDate = "";
        String longTime = "长期";
        String standardTime = "2018-01-11";

        System.out.println("emptyDate:" + transformDate(emptyDate));
        System.out.println("longTime:" + transformDate(longTime));
        System.out.println("standardTime:" + transformDate(standardTime));

    }

    private Date transformDate(String date) {

        if (StringUtils.isEmpty(date)) {
            return null;
        }

        if (date.equals("长期")) {
            return new SimpleDateFormat("yyyy-MM-dd").parse("2099-01-01", new ParsePosition(0));
        }

        return new SimpleDateFormat("yyyy-MM-dd").parse(date, new ParsePosition(0));
    }

    @Test
    public void testDate1(){
        System.out.println( new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @Test
    public void testSubString() {
        System.out.println("330104".substring(0, 4));
    }

    @Test
    public void testFile() {
        File file = new File("C:\\Users\\x4\\Desktop\\rootCertificate\\微信图片_20180830141150.jpg");
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println("fileName:" + fileName);
        System.out.println("后缀名" + suffix);
    }

    @Test// 右移动 除以2^ n, 左移 乘以2 ^n
    public void test12(){
        System.out.println(10 << 1);
    }


}
