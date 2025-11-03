package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;

import java.util.Optional;

/**
 * @author Alish
 */
public interface TraineeRepository extends UserRepository<Trainee> {
    void delete(String username);
    Optional<Trainee> findTraineeWithTrainers(String userName);
}
