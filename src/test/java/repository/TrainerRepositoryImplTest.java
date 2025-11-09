package repository;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import com.gymcrm.infrastructure.repository.TrainerRepositoryImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainerRepositoryImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Long> longQuery;

    @Mock
    TypedQuery<TrainerDao> trainerQuery;

    @InjectMocks
    TrainerRepositoryImpl repository;

    Trainer domainTrainer;
    TrainerDao trainerDao;

    @BeforeEach
    void init() {
        domainTrainer = new Trainer(new User(1L, "trainer", "pass", "T", "R", true),
                new TrainingType(10L, TrainingTypeEnum.FITNESS));

        trainerDao = TrainerMapper.toDao(domainTrainer);
    }

    @Test
    void save_shouldPersistTrainerDao_withArgThat() {
        repository.save(domainTrainer);
        verify(entityManager).persist(argThat((TrainerDao dao) ->
                dao.getId() == null &&
                        dao.getSpecialization().getName() == domainTrainer.getSpecialization().name() &&
                        dao.getUser().getUsername().equals(domainTrainer.getUserProfile().getUsername())
        ));
    }

    @Test
    void update_shouldMergeTrainerDao_withArgThat() {
        domainTrainer.setId(1L);
        repository.update(domainTrainer);
        verify(entityManager).merge(argThat((TrainerDao dao) ->
                dao.getId().equals(domainTrainer.getId()) &&
                        dao.getSpecialization().getName() == domainTrainer.getSpecialization().name() &&
                        dao.getUser().getUsername().equals(domainTrainer.getUserProfile().getUsername())
        ));
    }

    @Test
    void findIdByUsername_shouldReturnId() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), anyString())).thenReturn(longQuery);
        when(longQuery.getResultStream()).thenReturn(Stream.of(10L));
        Optional<Long> result = repository.findIdByUsername("trainer");
        assertTrue(result.isPresent());
        assertEquals(10L, result.get());
        verify(longQuery).setParameter("uName", "trainer");
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
    void findTrainerWithTrainees_shouldReturnMappedDomainObject() {
        TraineeDao traineeDao = new TraineeDao(100L, new UserDao(200L, "trainee",
                "pass", "T", "R", true),
                LocalDate.of(1990, 1, 1), "Address");

        trainerDao.getTrainees().add(traineeDao);
        when(entityManager.createQuery(anyString(), eq(TrainerDao.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("uName"), anyString())).thenReturn(trainerQuery);
        when(trainerQuery.getResultStream()).thenReturn(Stream.of(trainerDao));
        Optional<Trainer> result = repository.findTrainerWithTrainees("trainer");
        assertTrue(result.isPresent());
        Trainer trainer = result.get();
        assertEquals(1, trainer.getTrainees().size());
        assertEquals("Address", trainer.getTrainees().get(0).getAddress());
        verify(trainerQuery).setParameter("uName", "trainer");
    }

    @Test
    void findTrainerWithTrainees_shouldReturnEmptyIfNotFound() {
        when(entityManager.createQuery(anyString(), eq(TrainerDao.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("uName"), anyString())).thenReturn(trainerQuery);
        when(trainerQuery.getResultStream()).thenReturn(Stream.empty());
        Optional<Trainer> result = repository.findTrainerWithTrainees("nobody");
        assertFalse(result.isPresent());
    }

    @Test
    void getAvailableTrainersNotAssigned_shouldReturnTrainers() {
        when(entityManager.createQuery(anyString(), eq(TrainerDao.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), anyList())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(trainerDao));
        List<Trainer> result = repository.getAvailableTrainersNotAssigned(List.of(1L,2L));
        assertEquals(1, result.size());
        assertEquals(domainTrainer.getUserProfile().getUsername(), result.get(0).getUserProfile().getUsername());
        verify(trainerQuery).setParameter("assigned", List.of(1L,2L));
    }

    @Test
    void findTrainersByUserNamesIn_shouldReturnTrainers() {
        when(entityManager.createQuery(anyString(), eq(TrainerDao.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), anyList())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(trainerDao));
        List<Trainer> result = repository.findTrainersByUserNamesIn(List.of("trainer"));
        assertEquals(1, result.size());
        assertEquals("trainer", result.get(0).getUserProfile().getUsername());
        verify(trainerQuery).setParameter("uNames", List.of("trainer"));
    }

    @Test
    void findAssignedTrainersIds_shouldReturnIds() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), anyString())).thenReturn(longQuery);
        when(longQuery.getResultList()).thenReturn(List.of(10L));
        List<Long> result = repository.findAssignedTrainersIds("trainee");
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0));
        verify(longQuery).setParameter("uName", "trainee");

    }

    @Test
    void findAll_shouldReturnAllTrainers() {
        when(entityManager.createQuery(anyString(), eq(TrainerDao.class))).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(trainerDao));
        List<Trainer> result = repository.findAll();
        assertEquals(1, result.size());
        assertEquals("trainer", result.get(0).getUserProfile().getUsername());
    }
}
