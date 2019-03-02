package com.zz.amqp1.utils;

import java.util.UUID;

/**
 * Description:uuid生成的工具类
 * User: zhouzhou
 * Date: 2019-03-02
 * Time: 9:53 AM
 */
public class UUIDUtils {

    /**
     * 生成uuid无 - 符号
     * @return uuid
     */
    public static String generateUUID(){
       return UUID.randomUUID().toString().replace("-","");
    }
}
