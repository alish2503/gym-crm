package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.adapter.TraineeRepositoryImpl;
import com.gymcrm.infrastructure.persistence.jpa.TraineeJpaRepository;
import com.gymcrm.infrastructure.persistence.mapper.TraineeDaoMapper;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
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
class TraineeRepositoryImplTest {

    @Mock
    TraineeJpaRepository traineeJpaRepository;

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
        repository.saveOrUpdate(domainTrainee);
        verify(traineeJpaRepository).save(argThat((TraineeDao dao) ->
                dao.getId() == null &&
                        dao.getAddress().equals(domainTrainee.getAddress()) &&
                        dao.getDateOfBirth().equals(domainTrainee.getDateOfBirth()) &&
                        dao.getUser().getUsername().equals(domainTrainee.getUser().getUsername())
        ));
    }

    @Test
    void update_shouldMergeDao_withArgThat() {
        domainTrainee.setId(1L);
        repository.saveOrUpdate(domainTrainee);
        verify(traineeJpaRepository).save(argThat((TraineeDao dao) ->
                dao.getId().equals(domainTrainee.getId()) &&
                        dao.getAddress().equals(domainTrainee.getAddress()) &&
                        dao.getDateOfBirth().equals(domainTrainee.getDateOfBirth()) &&
                        dao.getUser().getUsername().equals(domainTrainee.getUser().getUsername())
        ));
    }

    @Test
    void findIdByUsername_shouldReturnId() {
        when(traineeJpaRepository.findIdByUsername(anyString())).thenReturn(Optional.of(10L));
        Optional<Long> result = repository.findTraineeId("john");
        assertTrue(result.isPresent());
        assertEquals(10L, result.get());
        verify(traineeJpaRepository).findIdByUsername("john");
    }

    @Test
    void findIdByUsername_shouldReturnEmptyIfNotFound() {
        when(traineeJpaRepository.findIdByUsername(anyString())).thenReturn(Optional.empty());
        Optional<Long> result = repository.findTraineeId("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void delete_shouldRemoveEntity() {
        repository.deleteTrainee(domainTrainee);
        verify(traineeJpaRepository).delete(
                argThat(dao -> dao.getUser().getUsername().equals("john"))
        );
    }

    @Test
    void findTraineeWithTrainers_shouldReturnMappedDomainObject() {
        TrainerDao trainerDao = new TrainerDao(100L, new UserDao(200L, "trainer",
                "pass", "T", "R", true),
                new TrainingTypeDao(5L, TrainingTypeEnum.FITNESS)
        );
        traineeDao.setTrainers(List.of(trainerDao));
        when(traineeJpaRepository.findWithTrainers(anyString())).thenReturn(Optional.of(traineeDao));
        Optional<Trainee> result = repository.findTraineeWithTrainers("john");
        assertTrue(result.isPresent());
        Trainee trainee = result.get();
        assertEquals("Address", trainee.getAddress());
        assertEquals(1, trainee.getTrainers().size());
        Trainer trainer = trainee.getTrainers().get(0);
        assertEquals(TrainingTypeEnum.FITNESS, trainer.getSpecialization().typeEnum());
        verify(traineeJpaRepository).findWithTrainers("john");
    }

    @Test
    void findTraineeWithTrainers_shouldReturnEmptyIfNotFound() {
        when(traineeJpaRepository.findWithTrainers(anyString())).thenReturn(Optional.empty());
        Optional<Trainee> result = repository.findTraineeWithTrainers("nobody");
        assertFalse(result.isPresent());
    }

    @Test
    void findTrainee_shouldReturnMappedDomainObject() {
        when(traineeJpaRepository.findByUserUsername(anyString())).thenReturn(Optional.of(traineeDao));
        Optional<Trainee> result = repository.findTrainee("john");
        assertTrue(result.isPresent());
        Trainee trainee = result.get();
        assertEquals("Address", trainee.getAddress());
        assertEquals(0, trainee.getTrainers().size());
        verify(traineeJpaRepository).findByUserUsername("john");
    }
}
