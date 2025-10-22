import com.gymcrm.dao.TrainingDao;
import com.gymcrm.exception.EntityNotFoundException;
import com.gymcrm.model.*;
import com.gymcrm.service.impl.TrainingServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer("Alex", "Stone", new TrainingType(TrainingTypeEnum.YOGA));
        trainer.setUsername("Alex.Stone");
        Trainee trainee = new Trainee("John", "Doe", LocalDate.of(1990, 5, 5), "NY");
        trainee.setUsername("John.Doe");
        training = new Training("Morning Yoga", new TrainingType(TrainingTypeEnum.YOGA),
                LocalDate.of(2025, 10, 22), 60, trainer, trainee);
        training.setId(1L);
    }

    @Test
    void testCreate_SavesTraining() {
        when(trainingDao.save(training)).thenReturn(training);
        Training saved = trainingService.create(training);
        verify(trainingDao).save(training);
        assertEquals(training, saved);
    }

    @Test
    void testGetById_WhenExists() {
        when(trainingDao.findById(1L)).thenReturn(Optional.of(training));
        Training found = trainingService.getById(1L);
        assertEquals(training, found);
    }

    @Test
    void testGetById_WhenNotFound() {
        when(trainingDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> trainingService.getById(1L));
    }

    @Test
    void testGetAll_ReturnsAllTrainings() {
        List<Training> trainings = List.of(training);
        when(trainingDao.findAll()).thenReturn(trainings);
        List<Training> result = trainingService.getAll();
        assertEquals(1, result.size());
        assertEquals(training, result.get(0));
    }
}
