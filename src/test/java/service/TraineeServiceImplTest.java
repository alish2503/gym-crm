package service;

import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.impl.TraineeServiceImpl;
import com.gymcrm.domain.model.*;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private AuthService authService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private UserCredentials creds;
    private User user;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        creds = new UserCredentials("John.Doe", "pass");
        user = new User("John.Doe", "hashed", "John", "Doe", true);
        trainee = new Trainee(LocalDate.of(1990, 1, 1), "oldAddr");
    }

    @Test
    void getTraineeByUserName_shouldReturnTraineeWithAuthenticatedUser() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        Trainee result = traineeService.getTraineeByUserName(creds);
        assertEquals(trainee, result);
        assertEquals(user, result.getUserProfile());
    }

    @Test
    void getTraineeByUserName_shouldThrowIfNotFound() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> traineeService.getTraineeByUserName(creds));
    }

    @Test
    void createTrainee_shouldCallCreateUserAndReturnCredentials() {
        CreateTraineeRequest req = new CreateTraineeRequest(true, "John", "Doe",
                LocalDate.of(2000, 1, 1), "addr");

        when(credentialService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("pass");
        when(credentialService.encodePassword("pass")).thenReturn("hashed");
        UserCredentials credentials = traineeService.createTrainee(req);
        assertEquals("John.Doe", credentials.username());
        assertEquals("pass", credentials.password());
        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository).save(captor.capture());
        Trainee saved = captor.getValue();
        assertEquals("addr", saved.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), saved.getDateOfBirth());
        assertNotNull(saved.getUserProfile());
        assertEquals("John.Doe", saved.getUserProfile().getUsername());
    }

    @Test
    void updateTrainee_shouldUpdateFields() {
        UpdateTraineeRequest req = new UpdateTraineeRequest(
                "john", "newP", "John", "Doe", true,
                LocalDate.of(2000,1,1),"newAddr"
        );

        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        when(credentialService.encodePassword("newP")).thenReturn("newHash");
        Trainee result = traineeService.updateTrainee(req, creds);
        assertEquals("newAddr", result.getAddress());
        assertEquals(LocalDate.of(2000,1,1), result.getDateOfBirth());
        verify(traineeRepository).update(trainee);
    }

    @Test
    void deleteTrainee_shouldDeleteById() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findIdByUsername("John.Doe")).thenReturn(Optional.of(42L));
        traineeService.deleteTrainee(creds);
        verify(traineeRepository).deleteById(42L);
    }

    @Test
    void deleteTrainee_shouldThrowIfIdNotFound() {
        when(authService.authenticate("John.Doe", "pass")).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> traineeService.deleteTrainee(creds));
    }

    @Test
    void updateTrainersForTrainee_shouldUpdateAssignedTrainers() {
        Trainer t1 = new Trainer(); t1.setUserProfile(new User("a","pass","A",
                "B",true));

        Trainer t2 = new Trainer(); t2.setUserProfile(new User("b","pass","B",
                "C",true));

        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1,t2));
        List<Trainer> updated = traineeService.updateTrainersForTrainee(creds, List.of("a","b"));
        assertEquals(2, updated.size());
        assertTrue(trainee.getTrainers().containsAll(updated));
        assertEquals(user, trainee.getUserProfile());
        verify(traineeRepository).update(trainee);
    }

    @Test
    void updateTrainersForTrainee_shouldThrowIfTrainerMissing() {
        Trainer t1 = new Trainer(); t1.setUserProfile(new User("a","pass","A",
                "B",true));

        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(traineeRepository.findTraineeWithTrainers("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findTrainersByUserNamesIn(List.of("a","b"))).thenReturn(List.of(t1));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainersForTrainee(creds, List.of("a","b")));

        assertTrue(ex.getMessage().contains("b"));
    }

    @Test
    void getAvailableTrainersForTrainee_shouldReturnAllIfNoAssigned() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(trainerRepository.findAssignedTrainersIds("John.Doe")).thenReturn(List.of());
        when(trainerRepository.findAll()).thenReturn(List.of(new Trainer(), new Trainer()));
        List<Trainer> result = traineeService.getAvailableTrainersForTrainee(creds);
        assertEquals(2, result.size());
    }

    @Test
    void getAvailableTrainersForTrainee_shouldReturnAvailableNotAssigned() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(trainerRepository.findAssignedTrainersIds("John.Doe")).thenReturn(List.of(1L, 2L));
        when(trainerRepository.getAvailableTrainersNotAssigned(List.of(1L,2L))).thenReturn(List.of(new Trainer()));
        List<Trainer> result = traineeService.getAvailableTrainersForTrainee(creds);
        assertEquals(1, result.size());
    }

    @Test
    void changePassword_shouldEncodeAndUpdateProfile() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(credentialService.encodePassword("newPass")).thenReturn("newHash");
        traineeService.changePassword(creds, "newPass");
        verify(userProfileRepository).updateProfile(user);
        assertEquals("newHash", user.getPassword());
    }

    @Test
    void activate_shouldSetActiveTrue() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        traineeService.activate(creds);
        verify(userProfileRepository).updateProfile(user);
        assertTrue(user.isActive());
    }

    @Test
    void deactivate_shouldSetActiveFalse() {
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        traineeService.deactivate(creds);
        verify(userProfileRepository).updateProfile(user);
        assertFalse(user.isActive());
    }
}
