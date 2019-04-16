package com.zz.amqp1.common.redistoken;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Description:分布式限流(令牌桶)实现类
 * <p>
 * 功能预览:
 *   1.初始化配置
 *   2.获取 token(立刻返回)
 *   2.1 获取 token 批量(立即返回)
 *   3.删除 token
 *   4.延时获取 token(必须获得,设置超时时间) 开发中

 * </p>
 * version: 1.0.0
 * User: zhouzhou
 * Date: 2019年04月16日15:30:32
 */
@Service
public class RateLimitClient {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Qualifier("getRedisScript")
    @Resource
    RedisScript<Long> ratelimitLua;
    @Qualifier("getInitRedisScript")
    @Resource
    RedisScript<Long> ratelimitInitLua;

    public Token initToken(RateLimiterParam limiterParam){
        Token token ;
        // 获取 redis 当前时间
        Long currMillSecond = stringRedisTemplate.execute(
                (RedisCallback<Long>) redisConnection -> redisConnection.time()
        );
        /**
         * redis.pcall("HMSET",KEYS[1],
         "last_mill_second",ARGV[1],
         "curr_permits",ARGV[2],
         "max_burst",ARGV[3],
         "rate",ARGV[4],
         "app",ARGV[5])
         */
        Long acquire = stringRedisTemplate.execute(ratelimitInitLua,
                Collections.singletonList(getKey(limiterParam.getName())),
                currMillSecond.toString(), // 当前时间
                "1", // 每次捞取数
                limiterParam.getMaxPermits()+"", // 桶最大容量
                limiterParam.getRate()+"", // 速率 n 次/秒
                limiterParam.getApp()); // app名字
        if (acquire == 1) {
            token = Token.SUCCESS;
        } else if (acquire == 0) {
            token = Token.SUCCESS;
        } else {
            token = Token.FAILED;
        }
        return token;
    }



    /**
     * 获取 token,一次;
     *
     * @param key
     * @return
     */
    public Boolean tryAcquire(String key) {
        return tryAcquire(key, 1);
    }

    /**
     * 获取 token, 批次
     * @param key
     * @param permits
     * @return
     */
    public Boolean tryAcquire(String key, Integer permits) {
        Token token = Token.SUCCESS;
        Long currMillSecond = stringRedisTemplate.execute(
                (RedisCallback<Long>) redisConnection -> redisConnection.time()
        );

        Long acquire = stringRedisTemplate.execute(ratelimitLua,
                Collections.singletonList(getKey(key)), permits.toString(), currMillSecond.toString());
        if (acquire == 1) {
            token = Token.SUCCESS;
        } else {
            token = Token.FAILED;
        }
        return token.isFailed();
    }

    public String getKey(String key) {
        return Constants.RATE_LIMIT_KEY + key;
    }

    /**
     * 删除令牌桶
     * @param key 令牌桶标志
     * @return 删除是否成功
     */
    public boolean deleteRateLimiter(String key){
        String rateLimiterKey = getKey(key);
        Boolean delete = stringRedisTemplate.delete(rateLimiterKey);
        return delete;
    }

}
