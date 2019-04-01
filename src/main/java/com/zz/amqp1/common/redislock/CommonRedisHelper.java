package com.zz.amqp1.common.redislock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: 通用Redis帮助类
 * User: zhouzhou
 * Date: 2018-09-05
 * Time: 15:39
 */
@Component
public class CommonRedisHelper {

    public static final String LOCK_PREFIX = "redis_lock";
    public static final int LOCK_EXPIRE = 300; // ms

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 加分布式锁
     *
     * @param track
     * @param sector
     * @param timeout
     * @return
     */
    public boolean setNx(String track, String sector, long timeout) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Boolean flag = valueOperations.setIfAbsent(track + sector, System.currentTimeMillis());
        // 如果成功设置超时时间, 防止超时
        if (flag) {
            valueOperations.set(track + sector, getLockValue(track, sector), timeout, TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 删除锁
     *
     * @param track
     * @param sector
     */
    public boolean delete(String track, String sector) {
       return redisTemplate.delete(track + sector);
    }

    /**
     * 查询锁
     * @return 写锁时间
     */
    public long getLockValue(String track, String sector) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        long createTime = (long) valueOperations.get(track + sector);
        return createTime;
    }

    /**
     *  最终加强分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean lock(String key){
        String lock = LOCK_PREFIX + key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());

            // 验锁
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

}
