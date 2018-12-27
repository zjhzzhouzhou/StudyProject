package com.zz.amqp.common.redislock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * redis锁注解,只为同步任务专属
 * @author zhouzhou
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedisLock {

    // 锁前缀
    String lockPrefix() default "";
    // 锁名
    String lockKey() default "";
    // 锁持续时间
    long timeOut() default 5;
    // 时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
