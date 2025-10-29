package assembler;

import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.assembler.TrainingAssembler;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TrainingAssemblerTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingAssembler trainingAssembler;

    private TrainingDao dao;
    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        dao = new TrainingDao("john", "anna", "YOGA",
                "Morning Yoga", LocalDate.now(), 60);

        trainee = new Trainee("John", "Doe", true, LocalDate.of(2000, 1, 1), "Address");
        trainer = new Trainer("Anna", "Smith", true, null);
    }

    @Test
    void mapToDomain_ShouldReturnTraining_WhenAllEntitiesExist() {
        when(traineeRepository.findByUsername("john")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("anna")).thenReturn(Optional.of(trainer));
        Training training = trainingAssembler.mapToDomain(dao);
        assertNotNull(training);
        assertEquals("Morning Yoga", training.trainingName());
        assertEquals(trainee, training.trainee());
        assertEquals(trainer, training.trainer());
        verify(traineeRepository).findByUsername("john");
        verify(trainerRepository).findByUsername("anna");
    }

    @Test
    void mapToDomain_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeRepository.findByUsername("john")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> trainingAssembler.mapToDomain(dao));

        assertTrue(ex.getMessage().contains("Trainee not found"));
    }

    @Test
    void mapToDomain_ShouldThrowException_WhenTrainerNotFound() {
        when(traineeRepository.findByUsername("john")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("anna")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> trainingAssembler.mapToDomain(dao));

        assertTrue(ex.getMessage().contains("Trainer not found"));
    }
}