package com.gymcrm.infrastructure.jpa;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author Alish
 */

public interface TrainingTypeJpaRepository extends JpaRepository<TrainingTypeDao, Long> {
    Optional<TrainingTypeDao> findByName(TrainingTypeEnum typeEnum);
    boolean existsByName(TrainingTypeEnum typeEnum);
}
