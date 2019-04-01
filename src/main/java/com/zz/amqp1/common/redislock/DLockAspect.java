package com.zz.amqp1.common.redislock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 分布式锁
 * <p>
 *     先获取锁, 获取不到则继续等待(指定时间), 失败次数(指定)次后跳出, 消费降级(抛出,系统繁忙稍后再试)
 *     如果没有重试次数,方法返回null 记得捕获NP
 *     当重试次数有, 但是重试间隔时间没写, 默认200ms 间隔
 * </p>
 * User: zhouzhou
 * Date: 2018-09-25
 * Time: 10:58
 * version: 1.0
 */
@Aspect
@Component
public class DLockAspect {

    private static final Logger log = LoggerFactory.getLogger(DLockAspect.class);
    private static final String LOCK_NAME = "lockName";
    private static final String RETRY_TIMES = "retryTimes";
    private static final String RETRY_WAIT = "retryWait";


    @Autowired
    private CommonRedisHelper redisHelper;

    @Pointcut("@annotation(com.zz.amqp1.common.redislock.DLock)")
    public void dLockAspect() {
    }

    @Around("dLockAspect()")
    public Object lockAroundAction(ProceedingJoinPoint proceeding) throws Throwable {
        System.out.println("分布式锁测试");
        //获取注解中的参数
        Map<String, Object> annotationArgs = this.getAnnotationArgs(proceeding);
        String lockName = (String) annotationArgs.get(LOCK_NAME);
        Assert.notNull(lockName, "分布式,锁名不能为空");
        int retryTimes = (int) annotationArgs.get(RETRY_TIMES);
        long retryWait = (long) annotationArgs.get(RETRY_WAIT);

        // 获取锁
        boolean lock = redisHelper.lock(lockName);
        if (lock) {
            // 执行主逻辑
            return execut(proceeding, lockName);
        } else {
            // 如果重试次数为零, 则不重试
            if (retryTimes <= 0) {
                log.info(String.format("{%s}已经被锁, 不重试", lockName));
                throw new RuntimeException(String.format("{%s}已经被锁, 不重试", lockName));
            }

            if (retryWait == 0) {
                retryWait = 200;
            }
            // 设置失败次数计数器, 当到达指定次数时, 返回失败
            int failCount = 1;
            while (failCount <= retryTimes) {
                // 等待指定时间ms
                try {
                    Thread.sleep(retryWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (redisHelper.lock(lockName)) {
                    // 执行主逻辑
                    return execut(proceeding, lockName);
                } else {
                    log.info(String.format("{%s}已经被锁, 正在重试[ %s/%s ],重试间隔{%s}毫秒", lockName, failCount, retryTimes, retryWait));
                    failCount++;
                }
            }
            throw new RuntimeException("系统繁忙, 请稍等再试");
        }

    }

    private Object execut(ProceedingJoinPoint proceeding, String lockName) throws Throwable {
        try {
            // 执行主逻辑
            Object proceed = proceeding.proceed();
            return proceed;
        } catch (Throwable throwable) {
            log.error(String.format("执行分布式锁发生异常锁名:{%s},异常名称:{%s}", lockName, throwable.getMessage()));
            throw throwable;
        } finally {
            redisHelper.delete(lockName);
        }
    }

    /**
     * 获取锁参数
     *
     * @param proceeding
     * @return
     */
    private Map<String, Object> getAnnotationArgs(ProceedingJoinPoint proceeding) {
        Class target = proceeding.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = proceeding.getSignature().getName();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Map<String, Object> result = new HashMap<String, Object>();
                DLock redisLock = method.getAnnotation(DLock.class);
                String firstArg = getFirstArg(proceeding);
                result.put(LOCK_NAME, redisLock.lockName().concat("_").concat(firstArg));
                result.put(RETRY_TIMES, redisLock.retryTimes());
                result.put(RETRY_WAIT, redisLock.retryWait());
                return result;
            }
        }
        return null;
    }

    /**
     * 获取第一个String类型的参数为锁的业务参数
     *
     * @param proceeding
     * @return
     */
    public String getFirstArg(ProceedingJoinPoint proceeding) {
        Object[] args = proceeding.getArgs();
        if (args != null && args.length > 0) {
            for (Object object : args) {
                if (object instanceof String) {
                    return (String) object;
                }
            }
        }
        return null;
    }

}
