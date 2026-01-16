package com.gymcrm.application.service.port;

import com.gymcrm.application.event.TrainerWorkloadEvent;

public interface TrainerWorkloadProducer {
    void sendMessage(TrainerWorkloadEvent message);
}
