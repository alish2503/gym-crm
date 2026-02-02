package com.gymcrm.infrastructure.messaging.producer;

import com.gymcrm.application.event.TrainerWorkloadEvent;
import com.gymcrm.application.service.port.TrainerWorkloadEventPublisher;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerWorkloadEventRabbitPublisher implements TrainerWorkloadEventPublisher {

    @Setter
    @Value("${queue-name}")
    private String queueName;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TrainerWorkloadEventRabbitPublisher(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @CircuitBreaker(name = "trainer-workload", fallbackMethod = "fallback")
    public void publish(TrainerWorkloadEvent trainerWorkloadEvent) {
        rabbitTemplate.convertAndSend(queueName, trainerWorkloadEvent, m -> {
            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                m.getMessageProperties().setHeader("transactionId", transactionId);
            }
            return m;
        });
    }

    private void fallback(TrainerWorkloadEvent event, Throwable ex) {
        log.error("RabbitMQ unavailable, fallback executed", ex);
    }
}

