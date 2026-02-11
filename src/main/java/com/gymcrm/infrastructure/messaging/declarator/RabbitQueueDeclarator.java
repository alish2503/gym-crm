package com.gymcrm.infrastructure.messaging.declarator;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("integration")
@Slf4j
public class RabbitQueueDeclarator {

    @Value("${queue-name}")
    private String queueName;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitQueueDeclarator(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void declareQueues() {
        String dlqName = queueName + "-dlq";
        Queue dlq = QueueBuilder.durable(dlqName).build();
        Queue queue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlqName)
                .build();

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareQueue(dlq);
        log.info("Declared queues: {} and {}", queueName, dlqName);
    }
}
