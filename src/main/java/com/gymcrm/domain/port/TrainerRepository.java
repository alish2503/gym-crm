package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainerRepository extends UserRepository<Trainer> {
    List<Trainer> getAvailableTrainersNotAssigned(Trainee trainee);
    List<Trainer> findTrainersByUserNamesIn(List<String> userNames);
    Optional<Trainer> findTrainerWithTrainees(String userName);
}
