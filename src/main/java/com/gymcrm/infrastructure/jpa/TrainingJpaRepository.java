package com.gymcrm.infrastructure.jpa;

import com.gymcrm.infrastructure.dao.TrainingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;

/**
 * @author Alish
 */

public interface TrainingJpaRepository extends JpaRepository<TrainingDao, Long>,
                                                JpaSpecificationExecutor<TrainingDao>
{
    boolean existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
            String trainerUsername, String traineeUsername, LocalDate trainingDate, String trainingName
    );
}
