package com.gymcrm.infrastructure.messaging;

import com.gymcrm.application.event.TrainerWorkloadEvent;
import com.gymcrm.infrastructure.messaging.producer.TrainerWorkloadProducerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadProducerImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TrainerWorkloadProducerImpl producer;
    private final String QUEUE_NAME = "test-queue";
    private TrainerWorkloadEvent event;

    @BeforeEach
    void setUp() {
        producer = new TrainerWorkloadProducerImpl(rabbitTemplate, QUEUE_NAME);
        event = new TrainerWorkloadEvent(
                null, null, null,
                null, null, null, null
        );
    }

    @Test
    void sendMessage_shouldSendMessageWithTransactionIdHeader() {
        MDC.put("transactionId", "12345");
        producer.sendMessage(event);
        ArgumentCaptor<MessagePostProcessor> processorCaptor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq(QUEUE_NAME),
                eq(event), processorCaptor.capture());

        Message message = new Message(new byte[0], new MessageProperties());
        Message processed = processorCaptor.getValue().postProcessMessage(message);
        assertEquals("12345", processed.getMessageProperties().getHeaders().get("transactionId"));
        MDC.clear();
    }

    @Test
    void sendMessage_shouldSendMessageWithoutTransactionIdHeaderIfMdcIsEmpty() {
        producer.sendMessage(event);
        ArgumentCaptor<MessagePostProcessor> processorCaptor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        verify(rabbitTemplate).convertAndSend(eq(QUEUE_NAME), eq(event), processorCaptor.capture());
        Message message = new Message(new byte[0], new MessageProperties());
        Message processed = processorCaptor.getValue().postProcessMessage(message);
        assertNull(processed.getMessageProperties().getHeaders().get("transactionId"));
    }
}
