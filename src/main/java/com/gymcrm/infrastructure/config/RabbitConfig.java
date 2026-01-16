package com.gymcrm.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${queue-name}")
    private String queueName;

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplateCustomizer rabbitTemplateCustomizer() {
        return rabbitTemplate -> {
            rabbitTemplate.setMandatory(true);
            rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                if (ack) {
                    log.debug("RabbitMQ ACK received");
                } else {
                    log.error("RabbitMQ NACK received. Cause: {}", cause);
                }
            });
            rabbitTemplate.setReturnsCallback(returned -> log.error("""
                            RabbitMQ RETURNED message
                            exchange={}
                            routingKey={}
                            replyCode={}
                            replyText={}
                            """,
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    returned.getReplyCode(),
                    returned.getReplyText()
            ));
        };
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public List<Queue> declareQueues(RabbitAdmin rabbitAdmin) {
        String dlqName = queueName + "-dlq";
        Queue dlq = QueueBuilder.durable(dlqName).build();
        Queue queue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlqName)
                .build();

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareQueue(dlq);
        return List.of(queue, dlq);
    }
}



