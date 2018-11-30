package com.zz.amqp1.controller;

import com.zz.amqp1.bean.Student;
import com.zz.amqp1.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Description: Redis 测试controller
 * User: zhouzhou
 * Date: 2018-11-29
 * Time: 5:29 PM
 */
@RestController
public class RedisController {

    private static String STU_KEY = "user:stu";
    private static String STR_KEY = "user:str";
    private static String EXPIRE_KEY = "user:expire";

    @Autowired
    private RedisUtil redisUtil;

    // ------------------ java对象测试 -------------------

    @RequestMapping(value = "/setValue/stu", method = RequestMethod.POST)
    public Object setStuValue() {
        Student student = Student.builder().name("周舟").age(new Random().nextInt(100)).sex("男").build();
        redisUtil.set(STU_KEY, student);
        return "插入成功" + student;
    }

    @RequestMapping(value = "/getValue/stu", method = RequestMethod.GET)
    public Object getStuValue() {
        return redisUtil.get(STU_KEY);
    }

    // ---------------------  字符串测试  ---------------------------

    @RequestMapping(value = "/setValue/str", method = RequestMethod.POST)
    public Object setStrValue() {
        redisUtil.set(STR_KEY, "zhouzhou" + new Random().nextInt(100));
        return "插入成功";
    }

    @RequestMapping(value = "/getValue/str", method = RequestMethod.GET)
    public Object getStrValue() {
        return redisUtil.get(STR_KEY);
    }

    // -----------------------  持续一定时间测试 --------------------

    @RequestMapping(value = "/setValue/expire", method = RequestMethod.POST)
    public Object setExpireValue() {
        redisUtil.set(EXPIRE_KEY, "expireValue" + new Random().nextInt(100), 10L);
        return "插入成功";
    }

    @RequestMapping(value = "/getValue/expire", method = RequestMethod.GET)
    public Object getExpireValue() {
        return redisUtil.get(EXPIRE_KEY);
    }


    @RequestMapping(value = "/deleteAllValue", method = RequestMethod.DELETE)
    public String deleteValue() {
        redisUtil.del(STU_KEY, STR_KEY, EXPIRE_KEY);
        return "删除成功";
    }


}
