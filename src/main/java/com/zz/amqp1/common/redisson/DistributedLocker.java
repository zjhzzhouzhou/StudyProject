package com.zz.amqp1.common.redisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-14
 * Time: 5:51 PM
 */
public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);



}
