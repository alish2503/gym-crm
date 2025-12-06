package infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.adapter.TrainingTypeRepositoryImpl;
import com.gymcrm.infrastructure.jpa.TrainingTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TrainingTypeRepositoryImplTest {

    @Mock
    TrainingTypeJpaRepository trainingTypeJpaRepository;

    @InjectMocks
    TrainingTypeRepositoryImpl repository;

    TrainingTypeDao dao;

    @BeforeEach
    void init() {
        dao = new TrainingTypeDao(1L, TrainingTypeEnum.FITNESS);
    }

    @Test
    void findByName_shouldReturnOptional() {
        when(trainingTypeJpaRepository.findByName(TrainingTypeEnum.FITNESS)).thenReturn(Optional.of(dao));
        Optional<TrainingType> result = repository.findByName(TrainingTypeEnum.FITNESS);
        assertTrue(result.isPresent());
        assertEquals(TrainingTypeEnum.FITNESS, result.get().typeEnum());
        verify(trainingTypeJpaRepository).findByName(TrainingTypeEnum.FITNESS);
    }

    @Test
    void findByName_shouldReturnEmptyIfNotFound() {
        when(trainingTypeJpaRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.empty());
        Optional<TrainingType> result = repository.findByName(TrainingTypeEnum.YOGA);
        assertFalse(result.isPresent());
    }

    @Test
    void existsByName_shouldReturnTrueIfExists() {
        when(trainingTypeJpaRepository.existsByName(TrainingTypeEnum.FITNESS)).thenReturn(true);
        assertTrue(repository.existsByName(TrainingTypeEnum.FITNESS));
    }

    @Test
    void existsByName_shouldReturnFalseIfNotExists() {
        when(trainingTypeJpaRepository.existsByName(TrainingTypeEnum.YOGA)).thenReturn(false);
        assertFalse(repository.existsByName(TrainingTypeEnum.YOGA));
    }

    @Test
    void findAll_shouldReturnAllTrainingTypes() {
        when(trainingTypeJpaRepository.findAll()).thenReturn(List.of(dao));
        List<TrainingType> result = repository.findAll();
        assertEquals(1, result.size());
        assertEquals(TrainingTypeEnum.FITNESS, result.get(0).typeEnum());
    }

    @Test
    public void testCount() {
        when(trainingTypeJpaRepository.count()).thenReturn(5L);
        long result = repository.count();
        assertEquals(5L, result);
    }
}
