package application;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.service.port.CredentialService;
import com.gymcrm.application.service.impl.TraineeServiceImpl;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
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
    private CredentialService credentialService;

    @Mock
    private PasswordEncoder encoder;

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
        verify(traineeRepository).save(captor.capture());
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
        verify(traineeRepository).update(trainee);
    }

    @Test
    void deleteTrainee_shouldDeleteById() {
        when(traineeRepository.findIdByUsername("John.Doe")).thenReturn(Optional.of(42L));
        traineeService.deleteTrainee("John.Doe");
        verify(traineeRepository).deleteById(42L);
    }

    @Test
    void deleteTrainee_shouldThrowIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> traineeService.deleteTrainee("John.Doe"));
    }

    @Test
    void updateTrainersForTrainee_shouldUpdateAssignedTrainers() {
        Trainer t1 = new Trainer(); t1.setUser(new User("a","pass","A",
                "B",true));

        Trainer t2 = new Trainer(); t2.setUser(new User("b","pass","B",
                "C",true));
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1,t2));
        List<Trainer> updated = traineeService.updateTrainersForTrainee("John.Doe", List.of("a","b"));
        assertEquals(2, updated.size());
        assertTrue(trainee.getTrainers().containsAll(updated));
        assertEquals(user, trainee.getUser());
        verify(traineeRepository).update(trainee);
    }

    @Test
    void updateTrainersForTrainee_shouldThrowIfTrainerMissing() {
        Trainer t1 = new Trainer(); t1.setUser(new User("a","pass","A",
                "B",true));

        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1));
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTrainersForTrainee("John.Doe", List.of("a","b")));

        assertTrue(ex.getMessage().contains("b"));
    }

    @Test
    void getAvailableTrainersForTrainee_shouldReturnAllIfNoAssigned() {
        when(userProfileRepository.existsByUserName("John.Doe")).thenReturn(true);
        when(trainerRepository.findAssignedTrainersIds("John.Doe")).thenReturn(List.of());
        when(trainerRepository.findAll()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> result = traineeService.getAvailableTrainersForTrainee("John.Doe");
        assertEquals(2, result.size());
    }

    @Test
    void getAvailableTrainersForTrainee_shouldReturnAvailableNotAssigned() {
        when(userProfileRepository.existsByUserName("John.Doe")).thenReturn(true);
        when(trainerRepository.findAssignedTrainersIds("John.Doe")).thenReturn(List.of(1L, 2L));
        when(trainerRepository.getAvailableTrainersNotAssigned(List.of(1L,2L))).thenReturn(List.of(new Trainer()));
        List<Trainer> result = traineeService.getAvailableTrainersForTrainee("John.Doe");
        assertEquals(1, result.size());
    }
}
