package assembler;

import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.assembler.TrainerAssembler;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainerAssemblerTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainerAssembler trainerAssembler;

    private TrainerDao trainerDao;

    @BeforeEach
    void setUp() {
        trainerDao = new TrainerDao();
        trainerDao.setUsername("trainer1");
        trainerDao.setFirstName("John");
        trainerDao.setLastName("Doe");
        trainerDao.setSpecialization("FITNESS");
    }

    @Test
    void mapToDomain_ShouldReturnTrainer_WhenTrainingTypeExists() {
        TrainingType trainingType = new TrainingType(TrainingTypeEnum.FITNESS);
        when(trainingTypeRepository.findByName("FITNESS")).thenReturn(Optional.of(trainingType));
        Trainer result = trainerAssembler.mapToDomain(trainerDao);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(trainingType.name(), result.getSpecialization().name());
        verify(trainingTypeRepository).findByName("FITNESS");
    }

    @Test
    void mapToDomain_ShouldThrowException_WhenTrainingTypeNotFound() {
        when(trainingTypeRepository.findByName("FITNESS")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> trainerAssembler.mapToDomain(trainerDao)
        );

        assertTrue(ex.getMessage().contains("Training type not found: FITNESS"));
        verify(trainingTypeRepository).findByName("FITNESS");
    }
}