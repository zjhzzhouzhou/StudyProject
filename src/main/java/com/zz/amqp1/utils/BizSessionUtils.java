package com.zz.amqp1.utils;

import com.zz.amqp1.bean.Student;
import com.zz.amqp1.bean.common.BizContext;
import com.zz.amqp1.bean.common.BizSession;

/**
 * Description: 业务会话工具类
 * User: zhouzhou
 * Date: 2018-12-11
 * Time: 2:03 PM
 */
public class BizSessionUtils {

    public static void setStudent(Student student){
        BizSession.currentSession().setStudent(student);
    }

    public static BizContext getBizContext(){
        return BizSession.currentSession();
    }

}
