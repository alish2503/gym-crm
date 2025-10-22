package com.gymcrm.dao;

import com.gymcrm.model.Training;

/**
 * @author Alish
 */
public interface TrainingDao extends BaseDao<Training, Long> {
    Training findById(Long id);
}
