package com.gymcrm.infrastructure.health;

import com.gymcrm.domain.port.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */

@Component
public class TrainingTypesHealthIndicator implements HealthIndicator {
    private final TrainingTypeRepository repo;

    @Autowired
    public TrainingTypesHealthIndicator(TrainingTypeRepository repo) {
        this.repo = repo;
    }

    @Override
    public Health health() {
        long count = repo.count();
        if (count > 0) {
            return Health.up().withDetail("trainingTypesCount", count).build();
        } else {
            return Health.down().withDetail("error", "Training types not loaded").build();
        }
    }
}
