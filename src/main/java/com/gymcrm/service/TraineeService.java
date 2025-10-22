package com.gymcrm.service;

import com.gymcrm.model.Trainee;

/**
 * @author Alish
 */
public interface TraineeService extends UserService<Trainee> {
    void delete(String username);
}
