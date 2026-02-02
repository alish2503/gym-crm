package com.gymcrm.component;

import com.gymcrm.application.service.port.TrainerWorkloadEventPublisher;
import com.gymcrm.BaseTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.mock;

@ActiveProfiles("component-test")
public abstract class BaseComponentTest extends BaseTest {
    protected BaseComponentTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate);
    }

    @TestConfiguration
    static class MockMessagingConfig {

        @Bean
        public TrainerWorkloadEventPublisher trainerWorkloadEventPublisher() {
            return mock(TrainerWorkloadEventPublisher.class);
        }
    }
}
