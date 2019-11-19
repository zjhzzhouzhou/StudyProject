package com.zz.amqp1.service.quartz.task;

import com.zz.amqp1.utils.job.BaseJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: qingshan
 * @Date: 2018/9/11 16:31
 * @Description: 咕泡学院，只为更好的你
 */
@DisallowConcurrentExecution
public class TestTask1 implements BaseJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + " " + sdf.format(date) + " Task1： ----窝窝头，一块钱4个嘿嘿----");
/*        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
