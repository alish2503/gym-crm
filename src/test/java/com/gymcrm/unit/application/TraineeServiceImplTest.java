package com.gymcrm.unit.application;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.impl.TraineeServiceImpl;
import com.gymcrm.application.service.port.TrainerWorkloadEventPublisher;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private CredentialService credentialService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TrainerWorkloadEventPublisher trainerWorkloadEventPublisher;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private User user;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        user = new User("John.Doe", "hashed", "John", "Doe", true);
        trainee = new Trainee(LocalDate.of(1990, 1, 1), "oldAddr");
        trainee.setUser(user);
    }

    @Test
    void getTraineeByUserName_shouldReturnTraineeWithAuthenticatedUser() {
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        Trainee result = traineeService.getTraineeByUsername("John.Doe");
        assertEquals(trainee, result);
        assertEquals(user, result.getUser());
    }

    @Test
    void getTraineeByUsername_shouldThrowIfNotFound() {
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> traineeService.getTraineeByUsername("John.Doe"));
    }

    @Test
    void createTrainee_shouldCallCreateUserAndReturnCredentials() {
        CreateTraineeRequest req = new CreateTraineeRequest("John", "Doe",
                LocalDate.of(2000, 1, 1), "addr");

        when(credentialService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("pass");
        when(encoder.encode("pass")).thenReturn("hashed");
        UserCredentials credentials = traineeService.createTrainee(req);
        assertEquals("John.Doe", credentials.username());
        assertEquals("pass", credentials.password());
        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository).saveOrUpdate(captor.capture());
        Trainee saved = captor.getValue();
        assertEquals("addr", saved.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), saved.getDateOfBirth());
        assertNotNull(saved.getUser());
        assertEquals("John.Doe", saved.getUser().getUsername());
    }

    @Test
    void updateTrainee_shouldUpdateFields() {
        UpdateTraineeRequest req = new UpdateTraineeRequest(
                "John.Doe", "John", "Doe", true,
                LocalDate.of(2000,1,1),"newAddr"
        );

        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        Trainee result = traineeService.updateTrainee(req);
        assertEquals("newAddr", result.getAddress());
        assertEquals(LocalDate.of(2000,1,1), result.getDateOfBirth());
        verify(traineeRepository).saveOrUpdate(trainee);
    }

    @Test
    void deleteTrainee_existingTrainee_callsDelete() {
        Training training = new Training(new TrainingType(1L, TrainingTypeEnum.YOGA),
                "Morning Yoga", LocalDate.of(2025,11,10),60,
                new Trainer(new User("trainer1","pass","T","R",
                        true), null));

        when(traineeRepository.findTrainee("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainingRepository.findTrainingsForTrainee("John.Doe",
                new TrainingFilter(null, null, null, null)))
                .thenReturn(List.of(training));

        traineeService.deleteTrainee("John.Doe");
        verify(traineeRepository).deleteTrainee(trainee);
        verify(trainingRepository).deleteAllTrainingsByTraineeUsername("John.Doe");
    }

    @Test
    void deleteTrainee_nonExistingTrainee_doesNotCallDelete() {
        when(traineeRepository.findTrainee("unknown")).thenReturn(Optional.empty());
        traineeService.deleteTrainee("unknown");
        verify(traineeRepository, never()).deleteTrainee(any());
        verify(trainingRepository, never()).deleteAllTrainingsByTraineeUsername("unknown");
    }

    @Test
    void updateTrainersForTrainee_shouldUpdateAssignedTrainers() {
        Trainer t1 = new Trainer(); t1.setUser(new User("a","pass","A",
                "B",true));

        Trainer t2 = new Trainer(); t2.setUser(new User("b","pass","B",
                "C",true));
        when(traineeRepository.findTrainee("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1,t2));
        List<Trainer> updated = traineeService.updateTrainersForTrainee("John.Doe", List.of("a","b"));
        assertEquals(2, updated.size());
        assertTrue(trainee.getTrainers().containsAll(updated));
        assertEquals(user, trainee.getUser());
        verify(traineeRepository).saveOrUpdate(trainee);
    }

    @Test
    void updateTrainersForTrainee_shouldThrowIfTrainerMissing() {
        Trainer t1 = new Trainer(); t1.setUser(new User("a","pass","A",
                "B",true));

        when(traineeRepository.findTrainee("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1));
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTrainersForTrainee("John.Doe", List.of("a","b")));

        assertTrue(ex.getMessage().contains("b"));
    }

    @Test
    void getAvailableTrainersForTrainee_shouldReturnAvailableNotAssigned() {
        when(userProfileRepository.existsByUserName("John.Doe")).thenReturn(true);
        when(trainerRepository.findAvailableTrainersNotAssignedAndActive("John.Doe")).
                thenReturn(List.of(new Trainer()));

        List<Trainer> result = traineeService.getAvailableTrainersForTrainee("John.Doe");
        assertEquals(1, result.size());
    }
}
