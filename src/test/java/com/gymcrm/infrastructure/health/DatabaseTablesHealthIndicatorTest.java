package com.gymcrm.infrastructure.health;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.health.contributor.Health;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class DatabaseTablesHealthIndicatorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DatabaseTablesHealthIndicator indicator;

    @Test
    void shouldReturnUpWhenQuerySucceeds() {
        when(entityManager.createNativeQuery("select 1")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1);
        Health health = indicator.health();
        assertEquals("UP", health.getStatus().getCode());
        Map<String, Object> details = health.getDetails();
        assertTrue(details.containsKey("dbQuery"));
        assertEquals("OK", details.get("dbQuery"));
    }

    @Test
    void shouldReturnDownWhenQueryFails() {
        when(entityManager.createNativeQuery("select 1")).thenThrow(new RuntimeException("DB error"));
        Health health = indicator.health();
        assertEquals("DOWN", health.getStatus().getCode());
        Map<String, Object> details = health.getDetails();
        assertTrue(details.containsKey("error"));
        assertEquals("Database query failed", details.get("error"));
        assertTrue(details.containsKey("exception"));
        assertNotNull(details.get("exception"));
    }
}