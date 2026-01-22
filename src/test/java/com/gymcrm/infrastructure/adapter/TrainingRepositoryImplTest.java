package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.persistence.adapter.TrainingRepositoryImpl;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import com.gymcrm.infrastructure.persistence.jpa.TrainingJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainingRepositoryImplTest {

    @Mock
    TrainingJpaRepository trainingJpaRepository;

    @InjectMocks
    TrainingRepositoryImpl repository;

    TraineeDao traineeDao;
    TrainerDao trainerDao;
    TrainingDao trainingDao;

    @BeforeEach
    void init() {
        traineeDao = new TraineeDao(1L, new UserDao(11L, "traineeUser", "pass",
                "A", "B", true), LocalDate.of(1990, 1, 1),
                "Address");

        trainerDao = new TrainerDao(2L,
                new UserDao(22L, "trainerUser",
                        "pass", "T", "R", true),
                new TrainingTypeDao(1L, TrainingTypeEnum.YOGA));

        trainingDao = new TrainingDao("Morning Yoga", LocalDate.of(2025, 5, 5),
                60, traineeDao, trainerDao, new TrainingTypeDao(1L, TrainingTypeEnum.YOGA));
    }

    @Test
    void findTrainingsForTrainee_withFilters_shouldReturnMappedTraining() {
        TrainingFilter filter = new TrainingFilter(LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 31), new FullName("T", "R"),
                TrainingTypeEnum.YOGA);

        when(trainingJpaRepository.findAll(any(Specification.class))).thenReturn(List.of(trainingDao));
        List<Training> trainings = repository.findTrainingsForTrainee("traineeUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        assertEquals(TrainingTypeEnum.YOGA, t.getType().typeEnum());
        assertNotNull(t.getTrainer());
        assertEquals("T", t.getTrainer().getUser().getFirstName());
        assertEquals(TrainingTypeEnum.YOGA, t.getTrainer().getSpecialization().typeEnum());
        verify(trainingJpaRepository).findAll(any(Specification.class));
    }

    @Test
    void findTrainingsForTrainee_withoutFilters_shouldReturnMappedTraining() {
        TrainingFilter filter = new TrainingFilter(null, null, null, null);
        when(trainingJpaRepository.findAll(any(Specification.class))).thenReturn(List.of(trainingDao));
        List<Training> trainings = repository.findTrainingsForTrainee("traineeUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        verify(trainingJpaRepository).findAll(any(Specification.class));
    }

    @Test
    void findTrainingsForTrainer_withFilters_shouldReturnMappedTraining() {
        TrainingFilter filter = new TrainingFilter(LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 31), new FullName("A", "B"), null);

        when(trainingJpaRepository.findAll(any(Specification.class))).thenReturn(List.of(trainingDao));
        List<Training> trainings = repository.findTrainingsForTrainer("trainerUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        assertNotNull(t.getTrainee());
        assertEquals("A", t.getTrainee().getUser().getFirstName());
        verify(trainingJpaRepository).findAll(any(Specification.class));
    }

    @Test
    void existsTraining_shouldReturnTrueIfExists() {
        when(trainingJpaRepository.existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
                anyString(), anyString(), any(), anyString())).thenReturn(true);

        boolean exists = repository.existsTraining("trainerUser", "traineeUser",
                LocalDate.of(2025, 5, 5), "Morning Yoga");

        assertTrue(exists);
        verify(trainingJpaRepository).existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
                anyString(), anyString(), any(), anyString());
    }

    @Test
    void existsTraining_shouldReturnFalseIfNotExists() {
        when(trainingJpaRepository.existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
                anyString(), anyString(), any(), anyString())).thenReturn(false);

        boolean exists = repository.existsTraining("trainerUser", "traineeUser",
                LocalDate.of(2025, 5, 5), "Morning Yoga");

        assertFalse(exists);
        verify(trainingJpaRepository).existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
                anyString(), anyString(), any(), anyString());
    }
}
