package com.zz.amqp1.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-17
 * Time: 19:14
 */
@Data
@Alias("TXhCodeEntity")
@TableName("t_xh_code")
public class TXhCodeEntity {

    private int id;
    private String telCode;
    private String xhCode;
    private int count;
}
