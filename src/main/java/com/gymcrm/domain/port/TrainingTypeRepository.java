package com.gymcrm.domain.port;

import com.gymcrm.domain.model.TrainingType;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainingTypeRepository {
    Optional<TrainingType> findByName(String name);
}
