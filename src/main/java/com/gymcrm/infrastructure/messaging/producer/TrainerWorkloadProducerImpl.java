package com.gymcrm.infrastructure.messaging.producer;

import com.gymcrm.application.event.TrainerWorkloadEvent;
import com.gymcrm.application.service.port.TrainerWorkloadProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerWorkloadProducerImpl implements TrainerWorkloadProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    @Autowired
    public TrainerWorkloadProducerImpl(RabbitTemplate rabbitTemplate,
                                       @Value("${queue-name}") String queueName)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    @CircuitBreaker(name = "trainer-workload", fallbackMethod = "fallback")
    public void sendMessage(TrainerWorkloadEvent trainerWorkloadEvent) {
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

