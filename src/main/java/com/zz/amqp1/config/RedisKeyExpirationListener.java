package com.zz.amqp1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * Description: Redis超时监听器
 * User: zhouzhou
 * Date: 2019-04-07
 * Time: 9:02 PM
 */
@Service
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    private static Logger log = LoggerFactory.getLogger(RedisKeyExpirationListener.class);

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取过期的key
        String expireKey = message.toString();
        System.out.println("终于失效了");
        log.debug("key is:"+ expireKey);
        System.out.println(expireKey);

    }
}