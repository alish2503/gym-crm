package service;

import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.application.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Trainer trainer;
    private Trainee trainee;
    private Training training;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("John.Doe", "password", "John", "Doe", true,
                new TrainingType(TrainingTypeEnum.FITNESS));
        trainee = new Trainee("Jane.Smith", "password", "Jane", "Smith", true,
                LocalDate.of(1995, 1, 1), "New York");
        training = new Training("Morning Cardio", new TrainingType(TrainingTypeEnum.FITNESS),
                LocalDate.of(2020, 4, 12), 90, trainer, trainee);
    }

    @Test
    void create_ShouldSaveTraining_WhenTrainerAndTraineeExist() {
        when(trainerRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainer));
        when(traineeRepository.findByUsername("Jane.Smith")).thenReturn(Optional.of(trainee));
        when(trainingRepository.save(training)).thenReturn(training);
        Training result = trainingService.create(training);
        assertEquals(training, result);
        verify(trainerRepository).findByUsername("John.Doe");
        verify(traineeRepository).findByUsername("Jane.Smith");
        verify(trainingRepository).save(training);
    }

    @Test
    void create_ShouldThrowException_WhenTrainerNotFound() {
        when(trainerRepository.findByUsername("John.Doe")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(training));

        assertEquals("Trainer not found: John.Doe", exception.getMessage());
        verify(trainerRepository).findByUsername("John.Doe");
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenTraineeNotFound() {
        when(trainerRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainer));
        when(traineeRepository.findByUsername("Jane.Smith")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainingService.create(training));

        assertEquals("Trainee not found: Jane.Smith", exception.getMessage());
        verify(trainerRepository).findByUsername("John.Doe");
        verify(traineeRepository).findByUsername("Jane.Smith");
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void getById_ShouldReturnTraining_WhenFound() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));
        Training result = trainingService.getById(1L);
        assertEquals(training, result);
        verify(trainingRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> trainingService.getById(1L));

        assertEquals("Training not found: 1", exception.getMessage());
        verify(trainingRepository).findById(1L);
    }
}
