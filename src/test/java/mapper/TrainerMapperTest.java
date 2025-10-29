package mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
class TrainerMapperTest {

    private Trainer trainer;
    private TrainerDao dao;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        specialization = new TrainingType(TrainingTypeEnum.FITNESS);
        trainer = new Trainer(
                "Jane.Smith",
                "securePass",
                "Jane",
                "Smith",
                true,
                specialization
        );
        dao = new TrainerDao();
        dao.setUsername("Jane.Smith");
        dao.setPassword("securePass");
        dao.setFirstName("Jane");
        dao.setLastName("Smith");
        dao.setActive(true);
        dao.setSpecialization("FITNESS");
    }

    @Test
    void toDao_ShouldMapDomainToDaoCorrectly() {
        TrainerDao result = TrainerMapper.toDao(trainer);
        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals("securePass", result.getPassword());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertTrue(result.isActive());
        assertEquals("FITNESS", result.getSpecialization());
    }

    @Test
    void toDomain_ShouldMapDaoToDomainCorrectly() {
        Trainer result = TrainerMapper.toDomain(, dao);
        assertNotNull(result);
        assertEquals("Jane.Smith", result.getUsername());
        assertEquals("securePass", result.getPassword());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertTrue(result.isActive());
        assertEquals(TrainingTypeEnum.FITNESS, result.getSpecialization().getName());
    }

    @Test
    void toDaoAndBack_ShouldReturnEquivalentObject() {
        TrainerDao mappedDao = TrainerMapper.toDao(trainer);
        Trainer remappedTrainer = TrainerMapper.toDomain(, mappedDao);
        assertEquals(trainer.getUsername(), remappedTrainer.getUsername());
        assertEquals(trainer.getPassword(), remappedTrainer.getPassword());
        assertEquals(trainer.getFirstName(), remappedTrainer.getFirstName());
        assertEquals(trainer.getLastName(), remappedTrainer.getLastName());
        assertEquals(trainer.isActive(), remappedTrainer.isActive());
        assertEquals(trainer.getSpecialization().getName(), remappedTrainer.getSpecialization().getName());
    }
}
