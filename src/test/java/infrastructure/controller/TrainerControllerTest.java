package infrastructure.controller;

import com.gymcrm.application.service.port.TrainerService;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.presentation.controller.impl.TrainerController;
import com.gymcrm.presentation.dto.request.UpdateTrainerDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController controller;

    @Test
    void getTrainerProfile_shouldReturnDto() {
        User user = new User("Mike.Black", "pass", "Mike", "Black",
                true);

        Trainer trainer = new Trainer(user, new TrainingType(1L, TrainingTypeEnum.FITNESS));
        when(trainerService.getTrainerByUsername("Mike.Black")).thenReturn(trainer);
        TrainerWithTraineesDto dto = controller.getTrainerProfile("Mike.Black");
        assertNotNull(dto);
        assertEquals("Mike", dto.getFirstName());
        assertEquals("Black", dto.getLastName());
        assertTrue(dto.isActive());
        assertEquals(0, dto.getTraineeDtos().size());
    }

    @Test
    void updateTrainerProfile_shouldReturnDtoAfterUpdate() {
        UpdateTrainerDto request = new UpdateTrainerDto();
        request.setFirstName("Michael");
        request.setLastName("Black");
        request.setActive(true);
        User user = new User("Mike.Black", "pass", "Michael", "Black",
                true);

        Trainer trainer = new Trainer(user, new TrainingType(1L, TrainingTypeEnum.FITNESS));
        when(trainerService.updateTrainer(any())).thenReturn(trainer);
        TrainerWithTraineesAfterUpdateDto dto = controller.updateTrainerProfile("Mike.Black", request);
        assertNotNull(dto);
        assertEquals("Michael", dto.getFirstName());
        assertEquals("Black", dto.getLastName());
        assertTrue(dto.isActive());
        assertEquals("Mike.Black", dto.getUsername());
        assertEquals(Collections.emptyList(), dto.getTraineeDtos());
    }
}
