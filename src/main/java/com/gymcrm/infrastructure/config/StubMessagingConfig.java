package com.gymcrm.infrastructure.config;

import com.gymcrm.application.service.port.TrainerWorkloadEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("no-integration")
public class StubMessagingConfig {

    @Bean
    public TrainerWorkloadEventPublisher trainerWorkloadEventPublisher() {
        return event -> {};
    }
}
