package com.skyxt.demo.handler;


import com.skyxt.demo.service.MqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author skyxt
 * @description
 */
@Component
public class JobHandler {

    @Autowired
    private MqService mqService;

    private Logger logger = LoggerFactory.getLogger(JobHandler.class);

    private int num = 1;

    @Scheduled(fixedRate = 60 * 1000, initialDelay = 1000)
    public void fixedSendMsg() {
        mqService.sendMessage(String.format("第%s条消息来了", num++));
        logger.info(String.format("第%s条消息来了", num-1));
    }
}
