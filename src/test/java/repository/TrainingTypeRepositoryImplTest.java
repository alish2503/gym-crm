package repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.repository.TrainingTypeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainingTypeRepositoryImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<TrainingTypeDao> trainingTypeQuery;

    @Mock
    TypedQuery<Long> longQuery;

    @InjectMocks
    TrainingTypeRepositoryImpl repository;

    TrainingTypeDao dao;

    @BeforeEach
    void init() {
        dao = new TrainingTypeDao(1L, TrainingTypeEnum.FITNESS);
    }

    @Test
    void findByName_shouldReturnOptional() {
        when(entityManager.createQuery(anyString(), eq(TrainingTypeDao.class))).thenReturn(trainingTypeQuery);
        when(trainingTypeQuery.setParameter(anyString(), any(TrainingTypeEnum.class))).thenReturn(trainingTypeQuery);
        when(trainingTypeQuery.getResultStream()).thenReturn(Stream.of(dao));
        Optional<TrainingType> result = repository.findByName(TrainingTypeEnum.FITNESS);
        assertTrue(result.isPresent());
        assertEquals(TrainingTypeEnum.FITNESS, result.get().typeEnum());
        verify(trainingTypeQuery).setParameter("typeName", TrainingTypeEnum.FITNESS);
    }

    @Test
    void findByName_shouldReturnEmptyIfNotFound() {
        when(entityManager.createQuery(anyString(), eq(TrainingTypeDao.class))).thenReturn(trainingTypeQuery);
        when(trainingTypeQuery.setParameter(anyString(), any(TrainingTypeEnum.class))).thenReturn(trainingTypeQuery);
        when(trainingTypeQuery.getResultStream()).thenReturn(Stream.empty());
        Optional<TrainingType> result = repository.findByName(TrainingTypeEnum.YOGA);
        assertFalse(result.isPresent());
    }

    @Test
    void existsByName_shouldReturnTrueIfExists() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(eq("typeName"), eq(TrainingTypeEnum.FITNESS))).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);
        assertTrue(repository.existsByName(TrainingTypeEnum.FITNESS));
    }

    @Test
    void existsByName_shouldReturnFalseIfNotExists() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(eq("typeName"), eq(TrainingTypeEnum.YOGA))).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);
        assertFalse(repository.existsByName(TrainingTypeEnum.YOGA));
    }
}
