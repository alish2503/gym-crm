package mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
class TraineeMapperTest {

    private Trainee trainee;
    private TraineeDao dao;
    private LocalDate dateOfBirth;

    @BeforeEach
    void setUp() {
        dateOfBirth = LocalDate.of(2000, 3, 15);
        trainee = new Trainee(
                "John.Doe",
                "securePass123",
                "John",
                "Doe",
                true,
                dateOfBirth,
                "123 Main St"
        );

        dao = new TraineeDao();
        dao.setUsername("John.Doe");
        dao.setPassword("securePass123");
        dao.setFirstName("John");
        dao.setLastName("Doe");
        dao.setActive(true);
        dao.setDateOfBirth(dateOfBirth);
        dao.setAddress("123 Main St");
    }

    @Test
    void toDao_ShouldMapDomainToDaoCorrectly() {
        TraineeDao result = TraineeMapper.toDao(trainee);
        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("securePass123", result.getPassword());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue(result.isActive());
        assertEquals(dateOfBirth, result.getDateOfBirth());
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void toDomain_ShouldMapDaoToDomainCorrectly() {
        Trainee result = TraineeMapper.ToDomain(dao);
        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("securePass123", result.getPassword());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue(result.isActive());
        assertEquals(dateOfBirth, result.getDateOfBirth());
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void toDaoAndBack_ShouldReturnEquivalentObject() {
        TraineeDao mappedDao = TraineeMapper.toDao(trainee);
        Trainee remappedTrainee = TraineeMapper.ToDomain(mappedDao);
        assertEquals(trainee.getUsername(), remappedTrainee.getUsername());
        assertEquals(trainee.getPassword(), remappedTrainee.getPassword());
        assertEquals(trainee.getFirstName(), remappedTrainee.getFirstName());
        assertEquals(trainee.getLastName(), remappedTrainee.getLastName());
        assertEquals(trainee.isActive(), remappedTrainee.isActive());
        assertEquals(trainee.getDateOfBirth(), remappedTrainee.getDateOfBirth());
        assertEquals(trainee.getAddress(), remappedTrainee.getAddress());
    }
}
