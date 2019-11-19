package com.zz.amqp1.config.quartz;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.DelegatingJob;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 *
 * 将Spring的对象注入到Quartz Job 1
 */
public class AdaptableJobFactory implements JobFactory {
	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler arg1) throws SchedulerException {
		 return newJob(bundle);
	}

	 public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
	        try {
	        	// 返回Job实例
	            Object jobObject = createJobInstance(bundle);
	            return adaptJob(jobObject);
	        }
	        catch (Exception ex) {
	            throw new SchedulerException("Job instantiation failed", ex);
	        }
	    }

	    // 通过反射的方式创建实例
	    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
	        Method getJobDetail = bundle.getClass().getMethod("getJobDetail");
	        Object jobDetail = ReflectionUtils.invokeMethod(getJobDetail, bundle);
	        Method getJobClass = jobDetail.getClass().getMethod("getJobClass");
	        Class jobClass = (Class) ReflectionUtils.invokeMethod(getJobClass, jobDetail);
	        return jobClass.newInstance();
	    }

	    protected Job adaptJob(Object jobObject) throws Exception {
	        if (jobObject instanceof Job) {
	            return (Job) jobObject;
	        }
	        else if (jobObject instanceof Runnable) {
	            return new DelegatingJob((Runnable) jobObject);
	        }
	        else {
	            throw new IllegalArgumentException("Unable to execute quartz class [" + jobObject.getClass().getName() +
	                    "]: only [org.quartz.Job] and [java.lang.Runnable] supported.");
	        }
	    }
}
