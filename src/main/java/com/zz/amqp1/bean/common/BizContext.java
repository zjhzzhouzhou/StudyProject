package com.zz.amqp1.bean.common;

import com.zz.amqp1.bean.Student;
import lombok.Data;

/**
 * Description:业务上下文
 * User: zhouzhou
 * Date: 2018-12-11
 * Time: 2:07 PM
 */
@Data
public class BizContext {

    private Student student;

    private String systemEnv;
}
