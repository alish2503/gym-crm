package com.gymcrm.infrastructure.adapter;

import com.gymcrm.infrastructure.feign.TrainerWorkloadFeignClient;
import com.gymcrm.infrastructure.port.TrainerWorkloadClient;
import com.gymcrm.presentation.dto.request.TrainerWorkloadEventDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerWorkloadClientImpl implements TrainerWorkloadClient {
    private final TrainerWorkloadFeignClient feignClient;

    @Autowired
    public TrainerWorkloadClientImpl(TrainerWorkloadFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    @CircuitBreaker(
            name = "trainer-workload-service",
            fallbackMethod = "fallbackSendEvent"
    )
    public void sendEvent(TrainerWorkloadEventDto event) {
        feignClient.sendEvent(event);
    }

    private void fallbackSendEvent(TrainerWorkloadEventDto event, Throwable ex) {
        log.warn("Trainer-workload service unavailable. Event skipped. Reason: {}", ex.getMessage());
    }
}
