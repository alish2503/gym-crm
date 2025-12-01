package infrastructure.health;

import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.health.TrainingTypesHealthIndicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.health.contributor.Health;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TrainingTypesHealthIndicatorTest {

    @Mock
    private TrainingTypeRepository repo;

    @InjectMocks
    private TrainingTypesHealthIndicator indicator;

    @Test
    void shouldReturnUpWhenTrainingTypesExist() {
        when(repo.count()).thenReturn(5L);
        Health health = indicator.health();
        assertEquals("UP", health.getStatus().getCode());
        Map<String, Object> details = health.getDetails();
        assertTrue(details.containsKey("trainingTypesCount"));
        assertEquals(5L, details.get("trainingTypesCount"));
    }

    @Test
    void shouldReturnDownWhenNoTrainingTypes() {
        when(repo.count()).thenReturn(0L);
        Health health = indicator.health();
        assertEquals("DOWN", health.getStatus().getCode());
        Map<String, Object> details = health.getDetails();
        assertTrue(details.containsKey("error"));
        assertEquals("Training types not loaded", details.get("error"));
    }
}
