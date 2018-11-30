package com.zz.amqp1.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 16:50
 */
@Service
public class SchedulingService {

    @Scheduled(cron = "0,30 * * * * ? ")
    public void hello(){
        System.out.println("每隔30秒定时任务测试,当前时间为:" + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));
    }
}
