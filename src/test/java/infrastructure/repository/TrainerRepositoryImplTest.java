package infrastructure.repository;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.jpa.TrainerJpaRepository;
import com.gymcrm.infrastructure.mapper.TrainerDaoMapper;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.dao.TrainerDao;
import com.gymcrm.infrastructure.dao.UserDao;
import com.gymcrm.infrastructure.adapter.TrainerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainerRepositoryImplTest {

    @Mock
    TrainerJpaRepository trainerJpaRepository;

    @InjectMocks
    TrainerRepositoryImpl repository;

    Trainer domainTrainer;
    TrainerDao trainerDao;

    @BeforeEach
    void init() {
        domainTrainer = new Trainer(new User(1L, "trainer", "pass", "T", "R", true),
                new TrainingType(10L, TrainingTypeEnum.FITNESS));

        trainerDao = TrainerDaoMapper.toDao(domainTrainer);
    }

    @Test
    void save_shouldPersistTrainerDao_withArgThat() {
        repository.saveOrUpdate(domainTrainer);
        verify(trainerJpaRepository).save(argThat((TrainerDao dao) ->
                dao.getId() == null &&
                        dao.getSpecialization().getName() == domainTrainer.getSpecialization().typeEnum() &&
                        dao.getUser().getUsername().equals(domainTrainer.getUser().getUsername())
        ));
    }

    @Test
    void update_shouldMergeTrainerDao_withArgThat() {
        domainTrainer.setId(1L);
        repository.saveOrUpdate(domainTrainer);
        verify(trainerJpaRepository).save(argThat((TrainerDao dao) ->
                dao.getId().equals(domainTrainer.getId()) &&
                        dao.getSpecialization().getName() == domainTrainer.getSpecialization().typeEnum() &&
                        dao.getUser().getUsername().equals(domainTrainer.getUser().getUsername())
        ));
    }

    @Test
    void findIdByUsername_shouldReturnId() {
        when(trainerJpaRepository.findIdByUsername(anyString())).thenReturn(Optional.of(10L));
        Optional<Long> result = repository.findTrainerId("trainer");
        assertTrue(result.isPresent());
        assertEquals(10L, result.get());
        verify(trainerJpaRepository).findIdByUsername("trainer");
    }

    @Test
    void findTrainerWithTrainees_shouldReturnMappedDomainObject() {
        TraineeDao traineeDao = new TraineeDao(100L, new UserDao(200L, "trainee",
                "pass", "T", "R", true),
                LocalDate.of(1990, 1, 1), "Address");

        trainerDao.getTrainees().add(traineeDao);
        when(trainerJpaRepository.findWithTrainees(anyString())).thenReturn(Optional.of(trainerDao));
        Optional<Trainer> result = repository.findTrainerWithTrainees("trainer");
        assertTrue(result.isPresent());
        Trainer trainer = result.get();
        assertEquals(1, trainer.getTrainees().size());
        assertEquals("Address", trainer.getTrainees().get(0).getAddress());
        verify(trainerJpaRepository).findWithTrainees("trainer");
    }

    @Test
    void findTrainerWithTrainees_shouldReturnEmptyIfNotFound() {
        when(trainerJpaRepository.findWithTrainees(anyString())).thenReturn(Optional.empty());
        Optional<Trainer> result = repository.findTrainerWithTrainees("nobody");
        assertFalse(result.isPresent());
    }

    @Test
    void getAvailableTrainersNotAssigned_shouldReturnTrainersAndActive() {
        when(trainerJpaRepository.findAvailableTrainersForTrainee(anyString())).thenReturn(List.of(trainerDao));
        List<Trainer> result = repository.findAvailableTrainersNotAssignedAndActive("trainee");
        assertEquals(1, result.size());
        assertEquals(domainTrainer.getUser().getUsername(), result.get(0).getUser().getUsername());
        verify(trainerJpaRepository).findAvailableTrainersForTrainee("trainee");
    }

    @Test
    void findTrainersByUserNamesIn_shouldReturnTrainers() {
        when(trainerJpaRepository.findByUserUsernameIn(List.of("trainer"))).thenReturn(List.of(trainerDao));
        List<Trainer> result = repository.findTrainersByUserNamesIn(List.of("trainer"));
        assertEquals(1, result.size());
        assertEquals("trainer", result.get(0).getUser().getUsername());
        verify(trainerJpaRepository).findByUserUsernameIn(List.of("trainer"));
    }
}
