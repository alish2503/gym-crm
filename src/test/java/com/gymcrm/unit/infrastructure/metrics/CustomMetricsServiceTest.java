package com.gymcrm.unit.infrastructure.metrics;

import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.metrics.CustomMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomMetricsServiceTest {

    @Mock
    private UserProfileRepository repo;

    @Mock
    private Callable<String> callable;
    private MeterRegistry registry;
    private CustomMetricsService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new CustomMetricsService(registry, repo);
    }

    @Test
    void gaugeShouldRegisterActiveUsers() {
        when(repo.countActiveUsers()).thenReturn(7L);
        double value = registry.get("custom.users.active").gauge().value();
        assertEquals(7.0, value);
    }

    @Test
    void recordTrainingCreatedShouldIncrementCounter() {
        service.recordTrainingCreated();
        service.recordTrainingCreated();
        double count = registry.get("custom.trainings.created").counter().count();
        assertEquals(2.0, count);
    }

    @Test
    void timeProcessingShouldMeasureExecutionTime() throws Exception {
        when(callable.call()).thenReturn("ok");
        String result = service.timeProcessing("custom.processing.time", callable);
        assertEquals("ok", result);
        verify(callable, times(1)).call();
        assertEquals(1, registry.get("custom.processing.time").timer().count());
    }
}
