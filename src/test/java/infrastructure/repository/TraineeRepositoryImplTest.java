package infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.mapper.TraineeDaoMapper;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.dao.TrainerDao;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.dao.UserDao;
import com.gymcrm.infrastructure.repository.TraineeRepositoryImpl;
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
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TraineeRepositoryImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Long> longQuery;

    @Mock
    TypedQuery<TraineeDao> traineeQuery;

    @InjectMocks
    TraineeRepositoryImpl repository;

    Trainee domainTrainee;
    TraineeDao traineeDao;

    @BeforeEach
    void init() {
        domainTrainee = new Trainee(new User(1L, "john", "pass", "John",
                "Doe", true), LocalDate.of(1990, 1, 1),
                "Address");

        traineeDao = TraineeDaoMapper.toDao(domainTrainee);
    }

    @Test
    void save_shouldPersistDao_withArgThat() {
        repository.save(domainTrainee);
        verify(entityManager).persist(argThat((TraineeDao dao) ->
                dao.getId() == null &&
                        dao.getAddress().equals(domainTrainee.getAddress()) &&
                        dao.getDateOfBirth().equals(domainTrainee.getDateOfBirth()) &&
                        dao.getUser().getUsername().equals(domainTrainee.getUser().getUsername())
        ));
    }

    @Test
    void update_shouldMergeDao_withArgThat() {
        domainTrainee.setId(1L);
        repository.update(domainTrainee);
        verify(entityManager).merge(argThat((TraineeDao dao) ->
                dao.getId().equals(domainTrainee.getId()) &&
                        dao.getAddress().equals(domainTrainee.getAddress()) &&
                        dao.getDateOfBirth().equals(domainTrainee.getDateOfBirth()) &&
                        dao.getUser().getUsername().equals(domainTrainee.getUser().getUsername())
        ));
    }

    @Test
    void findIdByUsername_shouldReturnId() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), anyString())).thenReturn(longQuery);
        when(longQuery.getResultStream()).thenReturn(Stream.of(10L));
        Optional<Long> result = repository.findIdByUsername("john");
        assertTrue(result.isPresent());
        assertEquals(10L, result.get());
        verify(longQuery).setParameter("uName", "john");
    }

    @Test
    void findIdByUsername_shouldReturnEmptyIfNotFound() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), anyString())).thenReturn(longQuery);
        when(longQuery.getResultStream()).thenReturn(Stream.empty());
        Optional<Long> result = repository.findIdByUsername("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_shouldRemoveEntity() {
        when(entityManager.find(TraineeDao.class, 1L)).thenReturn(traineeDao);
        repository.deleteById(1L);
        verify(entityManager).remove(traineeDao);
    }

    @Test
    void findTraineeWithTrainers_shouldReturnMappedDomainObject() {
        TrainerDao trainerDao = new TrainerDao(100L, new UserDao(200L, "trainer",
                "pass", "T", "R", true),
                new TrainingTypeDao(5L, TrainingTypeEnum.FITNESS)
        );
        traineeDao.setTrainers(List.of(trainerDao));
        when(entityManager.createQuery(anyString(), eq(TraineeDao.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter(anyString(), anyString())).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.of(traineeDao));
        Optional<Trainee> result = repository.findTraineeWithTrainers("john");
        assertTrue(result.isPresent());
        Trainee trainee = result.get();
        assertEquals("Address", trainee.getAddress());
        assertEquals(1, trainee.getTrainers().size());
        Trainer trainer = trainee.getTrainers().get(0);
        assertEquals(TrainingTypeEnum.FITNESS, trainer.getSpecialization().typeEnum());
        verify(traineeQuery).setParameter("uName", "john");
    }

    @Test
    void findTraineeWithTrainers_shouldReturnEmptyIfNotFound() {
        when(entityManager.createQuery(anyString(), eq(TraineeDao.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter(anyString(), anyString())).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.empty());
        Optional<Trainee> result = repository.findTraineeWithTrainers("nobody");
        assertFalse(result.isPresent());
    }

    @Test
    void findTrainee_shouldReturnMappedDomainObject() {
        when(entityManager.createQuery(anyString(), eq(TraineeDao.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter(anyString(), anyString())).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.of(traineeDao));
        Optional<Trainee> result = repository.findTrainee("john");
        assertTrue(result.isPresent());
        Trainee trainee = result.get();
        assertEquals("Address", trainee.getAddress());
        assertEquals(0, trainee.getTrainers().size());
        verify(traineeQuery).setParameter("uName", "john");
    }
}
