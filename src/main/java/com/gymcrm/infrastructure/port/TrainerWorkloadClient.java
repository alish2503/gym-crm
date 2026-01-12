package com.gymcrm.infrastructure.port;

import com.gymcrm.presentation.dto.request.TrainerWorkloadEventDto;

public interface TrainerWorkloadClient {
    void sendEvent(TrainerWorkloadEventDto event);
}
