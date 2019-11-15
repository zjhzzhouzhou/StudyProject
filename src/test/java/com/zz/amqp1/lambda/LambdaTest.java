package com.zz.amqp1.lambda;

import com.google.common.base.Supplier;
import com.zz.amqp1.bean.Student;
import org.junit.Test;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-10-30
 * Time: 10:16
 */
public class LambdaTest {

    @Test
    public void testConstruct() {
        Supplier<Student> aNew = Student::new;
        Student student = aNew.get();
        System.out.println(student);
    }

}
