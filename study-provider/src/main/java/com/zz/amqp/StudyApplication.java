package com.zz.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-24
 * Time: 17:36
 */
@SpringBootApplication
@ServletComponentScan
public class StudyApplication {
    public static void main(String[] args) {

        System.out.println("这是个学习包,有点意思");
        SpringApplication.run(StudyApplication.class, args);

    }
}
