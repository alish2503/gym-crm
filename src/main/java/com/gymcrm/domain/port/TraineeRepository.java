package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;

/**
 * @author Alish
 */
public interface TraineeRepository extends UserRepository<Trainee> {
    void delete(String username);
}
