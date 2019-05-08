package com.skyxt.demo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author skyxt
 * @description
 */
@Service
public class MqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.routeKey}")
    private String routeKey;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange, routeKey, message);
    }


}
