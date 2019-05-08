package com.skyxt.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * @author skyxt
 * @description
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DemoConfig {

    @Resource
    private Environment env;
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        //factory.setUri("amqp://yxt:qwer@10.0.45.222:5672");
        factory.setHost(env.getProperty("mq.host").trim());
        factory.setPort(Integer.parseInt(env.getProperty("mq.port").trim()));
        factory.setVirtualHost(env.getProperty("mq.vhost").trim());
        factory.setUsername(env.getProperty("mq.username").trim());
        factory.setPassword(env.getProperty("mq.pwd").trim());
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //开启事务
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }


    @Bean
    Queue queue() {
        //是否持久化
        boolean durable = StringUtils.hasText(env.getProperty("mq.queue.durable").trim()) &&
                Boolean.parseBoolean(env.getProperty("mq.queue.durable").trim());
        //仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = StringUtils.hasText(env.getProperty("mq.queue.exclusive").trim()) &&
                Boolean.parseBoolean(env.getProperty("mq.queue.exclusive").trim());
        //当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.hasText(env.getProperty("mq.queue.autoDelete").trim()) &&
                Boolean.parseBoolean(env.getProperty("mq.queue.autoDelete").trim());
        return new Queue(env.getProperty("mq.queue").trim(), durable, exclusive, autoDelete);
    }

    @Bean
    TopicExchange exchange() {
        //是否持久化
        boolean durable = StringUtils.hasText(env.getProperty("mq.exchange.durable").trim()) &&
                Boolean.parseBoolean(env.getProperty("mq.exchange.durable").trim());
        //当所有消费客户端连接断开后，是否自动交换器
        boolean autoDelete = StringUtils.hasText(env.getProperty("mq.exchange.autoDelete").trim()) &&
                Boolean.parseBoolean(env.getProperty("mq.exchange.autoDelete").trim());
        return new TopicExchange(env.getProperty("mq.exchange").trim(), durable, autoDelete);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(env.getProperty("mq.routeKey"));
    }

}
