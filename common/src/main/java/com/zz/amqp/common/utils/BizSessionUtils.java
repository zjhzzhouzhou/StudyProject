package com.zz.amqp.common.utils;

import com.zz.amqp.common.bean.BizContext;
import com.zz.amqp.common.bean.BizSession;
import com.zz.amqp.common.model.Student;

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
