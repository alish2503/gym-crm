package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Training;

import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainingRepository extends BaseRepository<Training> {
    Optional<Training> findById(Long id);
}
