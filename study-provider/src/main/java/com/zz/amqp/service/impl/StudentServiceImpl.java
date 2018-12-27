package com.zz.amqp.service.impl;

import com.zz.amqp.annotation.ZService;
import com.zz.amqp.common.utils.RedisUtil;
import com.zz.amqp.service.StudentService;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-12-24
 * Time: 16:54
 */
@ZService
public class StudentServiceImpl implements StudentService {

    @Override
    public String query(String name) {

        return "查找到姓名:" + name;
    }
}
