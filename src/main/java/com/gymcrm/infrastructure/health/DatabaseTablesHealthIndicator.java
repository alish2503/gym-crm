package com.gymcrm.infrastructure.health;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */

@Component
public class DatabaseTablesHealthIndicator implements HealthIndicator {

    @PersistenceContext
    EntityManager em;

    @Override
    public Health health() {
        try {
            em.createQuery("select 1").getSingleResult();
            return Health.up().withDetail("dbQuery", "OK").build();
        } catch (Exception e) {
            return Health.down().withDetail("error", "Database query failed").
                    withDetail("exception", e.getMessage()).build();
        }
    }
}