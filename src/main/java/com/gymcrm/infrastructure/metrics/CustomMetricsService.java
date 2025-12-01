package com.gymcrm.infrastructure.metrics;

import com.gymcrm.domain.port.UserProfileRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author Alish
 */

@Service
public class CustomMetricsService {
    private final MeterRegistry meterRegistry;

    @Autowired
    public CustomMetricsService(MeterRegistry meterRegistry, UserProfileRepository userProfileRepository) {
        this.meterRegistry = meterRegistry;
        meterRegistry.gauge("custom.users.active", userProfileRepository, UserProfileRepository::countActiveUsers);
    }

    public void recordTrainingCreated() {
        meterRegistry.counter("custom.trainings.created").increment();
    }

    public <T> T timeProcessing(String name, Callable<T> callable) throws Exception {
        return meterRegistry.timer(name).recordCallable(callable);
    }
}
