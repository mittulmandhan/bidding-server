package com.biddingserver.auctionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "auction_message_exchange";
    public static final String WINNER_QUEUE = "winner_queue";
    public static final String WINNER_ROUTING_KEY = "winner_routingKey";

    public static final String NOTIFY_QUEUE = "notify_queue";
    public static final String NOTIFY_ROUTING_KEY = "notify_routingKey";

    @Bean
    public Queue winnerQueue() {
        return new Queue(WINNER_QUEUE);
    }

    @Bean
    public Queue notifyQueue() {
        return new Queue(NOTIFY_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public List<Binding> bindings() {
        return Arrays.asList(
                BindingBuilder.bind(winnerQueue()).to(exchange()).with(WINNER_ROUTING_KEY),
                BindingBuilder.bind(notifyQueue()).to(exchange()).with(NOTIFY_ROUTING_KEY)
        );
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
