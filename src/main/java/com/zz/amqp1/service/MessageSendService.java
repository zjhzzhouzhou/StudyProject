package com.zz.amqp1.service;

import com.zz.amqp1.bean.MqModel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Description: 消息发送的业务层
 * User: zhouzhou
 * Date: 2018-08-28
 * Time: 15:36
 */
@Service
public class MessageSendService {

    @Value("${mq.exchange.exchange}")
    String exchange;

    @Value("${mq.exchange.queue}")
    String queue;

    @Value("${mq.exchange.routekey}")
    String route_key;

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * 发送消息
     */
    public void sendStringMessage(Object object) {
        MqModel mqModel = new MqModel();
        mqModel.setMqNo(System.currentTimeMillis() + "");
        mqModel.setMsg("这是一条消息");
        mqModel.setData(object);
        amqpTemplate.convertAndSend(exchange, route_key, mqModel);
    }

    // 初始化同步
    @PostConstruct
    private void init() {
        // 创建交换机
        amqpAdmin.declareExchange(new DirectExchange(exchange));
        // 创建队列
        amqpAdmin.declareQueue(new Queue(queue, true));
        // 建立绑定关系
        amqpAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, exchange, route_key, null));

    }
}
