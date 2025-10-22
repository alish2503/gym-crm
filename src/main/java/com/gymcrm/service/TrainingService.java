package com.gymcrm.service;

import com.gymcrm.model.Training;

/**
 * @author Alish
 */
public interface TrainingService extends BaseService<Training, Long> {
    Training getById(Long id);
}
