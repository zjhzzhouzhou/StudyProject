package com.zz.amqp1.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-08-27
 * Time: 17:40
 */
@Data
@Builder
public class Student {
    @Tolerate
    public Student() {
    }

    private String name;
    private int age;
    private String sex;

}
