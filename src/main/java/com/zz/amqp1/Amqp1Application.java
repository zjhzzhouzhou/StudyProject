package com.zz.amqp1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 自动配置了哪些:
 * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
 * 1.自动配置ConnectionFactory =>spring.rabbitmq RabbitProperties
 * 2.RabbitTemplate :rabbitMq发送和接收消息
 * 3.AmqpAdmin RabbitMQ系统功能管理组件
 * 4.@EnableRabbit + @RabbitListener 来监听队列里的内容
 */
@EnableRabbit // 开启基于注解的rabbitMq
@EnableAsync // 开启异步任务
//@EnableScheduling // 开启定时任务,弃用，改用quartz
@SpringBootApplication
@ComponentScan("com.zz")
@EnableAspectJAutoProxy(proxyTargetClass=true)
@MapperScan("com.zz.amqp1.dao.repository")
public class Amqp1Application {

    public static void main(String[] args) {
        SpringApplication.run(Amqp1Application.class, args);
    }
}
