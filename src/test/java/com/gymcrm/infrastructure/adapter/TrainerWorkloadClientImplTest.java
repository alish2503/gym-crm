package com.gymcrm.infrastructure.adapter;

import com.gymcrm.infrastructure.feign.TrainerWorkloadFeignClient;
import com.gymcrm.application.event.TrainerWorkloadEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadClientImplTest {

    @Mock
    private TrainerWorkloadFeignClient feignClient;

    @InjectMocks
    private TrainerWorkloadClientImpl trainerWorkloadClient;

    @Test
    void sendEvent_callsFeignClientSuccessfully() {
        TrainerWorkloadEvent event = new TrainerWorkloadEvent(null, null, null,
                true, null, 0, null);

        trainerWorkloadClient.sendEvent(event);
        verify(feignClient, times(1)).sendEvent(event);
    }
}

