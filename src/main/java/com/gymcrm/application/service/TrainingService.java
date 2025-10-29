package com.gymcrm.application.service;

import com.gymcrm.domain.model.Training;

/**
 * @author Alish
 */
public interface TrainingService extends BaseService<Training> {
    Training getById(Long id);
}
