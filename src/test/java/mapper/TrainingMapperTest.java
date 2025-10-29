package mapper;

import com.gymcrm.domain.model.*;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alish
 */
class TrainingMapperTest {

    private Trainer trainer;
    private Trainee trainee;
    private Training training;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        date = LocalDate.of(2024, 5, 10);
        TrainingType trainingType = new TrainingType(TrainingTypeEnum.YOGA);
        trainer = new Trainer("Anna", "Smith", true, trainingType);
        trainer.setUsername("Anna.Smith");
        trainee = new Trainee("John", "Doe", true, LocalDate.of(2000, 1, 1), "Astana");
        trainee.setUsername("John.Doe");
        training = new Training(
                "Morning Yoga",
                trainingType,
                date,
                60,
                trainer,
                trainee
        );
    }

    @Test
    void toDao_ShouldMapDomainToDaoCorrectly() {
        TrainingDao dao = TrainingMapper.toDao(training);
        assertNotNull(dao);
        assertEquals("John.Doe", dao.getTraineeUsername());
        assertEquals("Anna.Smith", dao.getTrainerUsername());
        assertEquals("YOGA", dao.getTrainingTypeName());
        assertEquals("Morning Yoga", dao.getTrainingName());
        assertEquals(date, dao.getTrainingDate());
        assertEquals(60, dao.getDuration());
    }

    @Test
    void toDomain_ShouldMapDaoToDomainCorrectly() {
        TrainingDao dao = new TrainingDao(
                "John.Doe",
                "Anna.Smith",
                "YOGA",
                "Morning Yoga",
                date,
                60
        );

        Training result = TrainingMapper.toDomain(dao, trainer, trainee);
        assertNotNull(result);
        assertEquals("Morning Yoga", result.trainingName());
        assertEquals(date, result.trainingDate());
        assertEquals(60, result.duration());
        assertEquals(trainer, result.trainer());
        assertEquals(trainee, result.trainee());
        assertEquals(trainer.getSpecialization(), result.type());
    }
}
