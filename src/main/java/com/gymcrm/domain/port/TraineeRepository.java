package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;

import java.util.Optional;

/**
 * @author Alish
 */
public interface TraineeRepository extends UserRepository<Trainee> {
    void deleteById(Long id);
    Optional<Trainee> findTraineeWithTrainers(String username);
}
