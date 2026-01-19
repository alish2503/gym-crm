package com.gymcrm.application.service.port;

import com.gymcrm.application.event.TrainerWorkloadEvent;

public interface TrainerWorkloadEventPublisher {
    void publish(TrainerWorkloadEvent event);
}
