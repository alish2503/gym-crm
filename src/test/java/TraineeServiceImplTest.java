import com.gymcrm.dao.TraineeDao;
import com.gymcrm.exception.EntityNotFoundException;
import com.gymcrm.model.Trainee;
import com.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee("John", "Doe", LocalDate.of(1995, 1, 1), "NY");
    }

    @Test
    void testCreate_AssignsUsernameAndPassword() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
        Trainee created = traineeService.create(trainee);
        assertNotNull(created.getPassword());
        assertTrue(created.getUsername().startsWith("John.Doe"));
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    void testCreate_AssignsUsernameWithSuffixWhenExists() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(traineeDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
        Trainee newTrainee = new Trainee("John", "Doe", LocalDate.of(1990, 2, 2), "LA");
        Trainee created = traineeService.create(newTrainee);
        assertEquals("John.Doe1", created.getUsername());
        verify(traineeDao).save(created);
    }

    @Test
    void testGetByUsername_WhenExists() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        Trainee found = traineeService.getByUsername("John.Doe");
        assertEquals(trainee, found);
    }

    @Test
    void testGetByUsername_WhenNotFound() {
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> traineeService.getByUsername("John.Doe"));
    }

    @Test
    void testUpdate_WhenExists() {
        trainee.setUsername("John.Doe");
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        traineeService.update(trainee);
        verify(traineeDao).update(trainee);
    }

    @Test
    void testUpdate_WhenNotExists() {
        trainee.setUsername("John.Doe");
        when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> traineeService.update(trainee));
    }

    @Test
    void testDelete_CallsDaoDelete() {
        traineeService.delete("John.Doe");
        verify(traineeDao).delete("John.Doe");
    }

    @Test
    void testGetAll_ReturnsAllTrainees() {
        List<Trainee> trainees = List.of(trainee);
        when(traineeDao.findAll()).thenReturn(trainees);
        List<Trainee> result = traineeService.getAll();
        assertEquals(1, result.size());
        assertEquals(trainee, result.get(0));
    }
}


