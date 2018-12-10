package com.zz.amqp1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-14
 * Time: 11:32
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test1(){
        String url ="http://127.0.01:8080/sendstr";
        String msg = restTemplate.getForObject(url, String.class);
        System.out.println("----------------------------- \n -" + msg);
    }

}
