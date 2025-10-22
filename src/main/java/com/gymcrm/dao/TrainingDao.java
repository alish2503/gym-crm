package com.gymcrm.dao;

import com.gymcrm.model.Training;

import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainingDao extends BaseDao<Training, Long> {
    Optional<Training> findById(Long id);
}
