package service;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.PasswordService;
import com.gymcrm.application.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("John", "Doe", true,
                new TrainingType(TrainingTypeEnum.ZUMBA));
    }

    @Test
    void testCreateTrainer_Success() {
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordService.generateRandomPassword(anyInt())).thenReturn(trainer.getPassword());
        Trainer created = trainerService.create(trainer);
        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testGetByUsername_Success() {
        when(trainerRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainer));
        Trainer found = trainerService.getByUsername("John.Doe");
        assertNotNull(found);
        assertEquals("John", found.getFirstName());
        assertEquals(TrainingTypeEnum.ZUMBA, found.getSpecialization().getName());
    }
}