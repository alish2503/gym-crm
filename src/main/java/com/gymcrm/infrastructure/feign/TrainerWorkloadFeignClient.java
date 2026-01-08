package com.gymcrm.infrastructure.feign;

import com.gymcrm.infrastructure.config.TrainerWorkloadFeignConfig;
import com.gymcrm.presentation.dto.request.TrainerWorkloadEventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${service-name.trainer-workload}",
        configuration = TrainerWorkloadFeignConfig.class
)
public interface TrainerWorkloadFeignClient {

    @PostMapping("/workload")
    void sendEvent(@RequestBody TrainerWorkloadEventDto event);
}