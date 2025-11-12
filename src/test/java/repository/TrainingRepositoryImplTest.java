package repository;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import com.gymcrm.infrastructure.repository.TrainingRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainingRepositoryImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<TrainingDao> trainingQuery;

    @Mock
    TypedQuery<Long> longQuery;

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
                new com.gymcrm.infrastructure.persistence.dao.UserDao(22L, "trainerUser",
                        "pass", "T", "R", true),
                new TrainingTypeDao(1L, TrainingTypeEnum.YOGA));

        trainingDao = new TrainingDao("Morning Yoga", LocalDate.of(2025, 5, 5),
                60, traineeDao, trainerDao, new TrainingTypeDao(1L, TrainingTypeEnum.YOGA));
    }

    @Test
    void findTrainingsForTrainee_withAllFilters_shouldReturnMappedTraining() {
        when(entityManager.createQuery(anyString(), eq(TrainingDao.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter(anyString(), any())).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(List.of(trainingDao));
        TrainingFilter filter = new TrainingFilter(LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 31), new FullName("T", "R"),
                TrainingTypeEnum.YOGA
        );
        List<Training> trainings = repository.findTrainingsForTrainee("traineeUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        assertEquals(TrainingTypeEnum.YOGA, t.getType().name());
        assertNotNull(t.getTrainer());
        assertEquals("T", t.getTrainer().getUser().getFirstName());
        assertEquals(TrainingTypeEnum.YOGA, t.getTrainer().getSpecialization().name());
        verify(trainingQuery).setParameter("uname", "traineeUser");
        verify(trainingQuery).setParameter("from", filter.from());
        verify(trainingQuery).setParameter("to", filter.to());
        verify(trainingQuery).setParameter("fName", "T");
        verify(trainingQuery).setParameter("lName", "R");
        verify(trainingQuery).setParameter("tType", TrainingTypeEnum.YOGA);
    }

    @Test
    void findTrainingsForTrainee_withoutFilters_shouldReturnMappedTraining() {
        when(entityManager.createQuery(anyString(), eq(TrainingDao.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter(anyString(), any())).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(List.of(trainingDao));
        TrainingFilter filter = new TrainingFilter(null, null, null, null);
        List<Training> trainings = repository.findTrainingsForTrainee("traineeUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        verify(trainingQuery).setParameter("uname", "traineeUser");
        verify(trainingQuery, never()).setParameter(eq("from"), any());
        verify(trainingQuery, never()).setParameter(eq("to"), any());
        verify(trainingQuery, never()).setParameter(eq("fName"), any());
        verify(trainingQuery, never()).setParameter(eq("lName"), any());
        verify(trainingQuery, never()).setParameter(eq("tType"), any());
    }

    @Test
    void findTrainingsForTrainer_withFilters_shouldReturnMappedTraining() {
        when(entityManager.createQuery(anyString(), eq(TrainingDao.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter(anyString(), any())).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(List.of(trainingDao));
        TrainingFilter filter = new TrainingFilter(LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 31), new FullName("A", "B"), null);

        List<Training> trainings = repository.findTrainingsForTrainer("trainerUser", filter);
        assertEquals(1, trainings.size());
        Training t = trainings.get(0);
        assertEquals("Morning Yoga", t.getName());
        assertNotNull(t.getTrainee());
        assertEquals("A", t.getTrainee().getUser().getFirstName());
        verify(trainingQuery).setParameter("uname", "trainerUser");
        verify(trainingQuery).setParameter("from", filter.from());
        verify(trainingQuery).setParameter("to", filter.to());
        verify(trainingQuery).setParameter("fName", "A");
        verify(trainingQuery).setParameter("lName", "B");
    }

    @Test
    void existsTraining_shouldReturnTrueIfExists() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);
        boolean exists = repository.existsTraining("trainerUser", "traineeUser",
                LocalDate.of(2025, 5, 5), "Morning Yoga");

        assertTrue(exists);
    }

    @Test
    void existsTraining_shouldReturnFalseIfNotExists() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);
        boolean exists = repository.existsTraining("trainerUser", "traineeUser",
                LocalDate.of(2025, 5, 5), "Morning Yoga");

        assertFalse(exists);
    }
}
