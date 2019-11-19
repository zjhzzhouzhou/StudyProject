package com.zz.amqp1.service.quartz.task;

import com.zz.amqp1.utils.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: qingshan
 * @Date: 2018/9/11 17:15
 * @Description: 咕泡学院，只为更好的你
 */
public class TestTask2 implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + " " +sdf.format(date) + " Task2： ----谁他妈的买小米啊----");
    }
}
