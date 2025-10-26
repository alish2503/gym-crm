package service;

import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.application.service.PasswordService;
import com.gymcrm.service.TraineeServiceImpl;
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
class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(
                "John", "Doe", true,
                LocalDate.of(1995, 5, 20),
                "123 Street, City"
        );
    }

    @Test
    void testCreate_ShouldGenerateUsernameAndSave() {
        when(traineeRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.of(trainee))
                .thenReturn(Optional.empty());

        when(passwordService.generateRandomPassword(10)).thenReturn("securePass");
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));
        Trainee created = traineeService.create(trainee);
        assertNotNull(created);
        assertTrue(created.getUsername().startsWith("John.Doe"));
        assertEquals("securePass", created.getPassword());
        verify(traineeRepository, times(2)).findByUsername(anyString());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testGetByUsername_ShouldReturnTrainee_WhenExists() {
        when(traineeRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.of(trainee));

        trainee.setUsername("John.Doe");
        Trainee found = traineeService.getByUsername("John.Doe");
        assertNotNull(found);
        assertEquals("John.Doe", found.getUsername());
        verify(traineeRepository).findByUsername("John.Doe");
    }

    @Test
    void testGetByUsername_ShouldThrowException_WhenNotFound() {
        when(traineeRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.getByUsername("John.Doe"));
    }

    @Test
    void testUpdate_ShouldUpdateExistingTrainee() {
        trainee.setUsername("John.Doe");
        trainee.setPassword("oldPass");
        when(traineeRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.of(trainee));

        traineeService.update(trainee);
        verify(traineeRepository).findByUsername("John.Doe");
        verify(traineeRepository).update(any(Trainee.class));
    }

    @Test
    void testUpdate_ShouldThrow_WhenPasswordChanged() {
        Trainee existing = new Trainee("John.Doe", "oldPass", "John", "Doe", true,
                LocalDate.of(1995, 5, 20), "Address");
        Trainee updated = new Trainee("John.Doe", "newPass", "John", "Doe", true,
                LocalDate.of(1995, 5, 20), "Address");

        when(traineeRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> traineeService.update(updated));
    }

    @Test
    void testDelete_ShouldRemoveTrainee() {
        traineeService.delete("John.Doe");
        verify(traineeRepository).delete("John.Doe");
    }
}


