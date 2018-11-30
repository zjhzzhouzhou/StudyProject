package com.zz.amqp1;

import com.zz.amqp1.service.share.ShareTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Scanner;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-07
 * Time: 10:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShareToolTest {

    @Test
    public void test1(){
        Scanner scanneer = new Scanner(System.in);


        while(true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ShareTool().initHandler("1").print();
                }
            },"1号线程").start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ShareTool().initHandler("2").print();
                }
            },"2号线程").start();
            int a = scanneer.nextInt();
            if (a == 1){
                break;
            }
        }
    }
}
