package com.gymcrm.infrastructure.persistence.jpa;

import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("DELETE FROM TrainingDao t WHERE t.trainee.user.username = :username")
    void deleteAllByTraineeUsername(@Param("username") String username);
}
