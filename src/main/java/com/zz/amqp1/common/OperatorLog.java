package com.zz.amqp1.common;

import lombok.Data;

import java.util.Date;

/**
 * Description:操作日志类
 * User: zhouzhou
 * Date: 2019-03-28
 * Time: 3:49 PM
 */
@Data
public class OperatorLog {

    private String beanName;
    private String curUser;
    private String method;
    private String httpMethod;
    private String params;
    private String remoteAddr;
    private String sessionId;
    private String requestUrl;
    private long RequestStartTime;
    private Date createDate;
    private String userAgent;
    private Object result;
    private Long requestTime;
    private Long requestEndTime;

}
