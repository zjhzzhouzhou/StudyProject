package com.zz.amqp1;

import com.zz.amqp1.utils.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-11-16
 * Time: 10:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextUtilTest {

    @Test
    public void test(){
        String userId = SpringContextUtil.getPropertiesValue("userId");
        System.out.println(userId);
    }
}
