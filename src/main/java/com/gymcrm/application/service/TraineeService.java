package com.gymcrm.application.service;

import com.gymcrm.domain.model.Trainee;

/**
 * @author Alish
 */
public interface TraineeService extends UserService<Trainee> {
    void delete(String username);
}
