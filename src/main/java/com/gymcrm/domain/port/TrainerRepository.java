package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainerRepository extends UserRepository<Trainer> {
    Optional<Trainer> findTrainerWithTrainees(String username);
    List<Trainer> getAvailableTrainersNotAssignedAndActive(List<Long> assignedIds);
    List<Trainer> findTrainersByUserNamesIn(List<String> usernames);
    List<Long> findAssignedTrainersIds(String traineeUsername);
    List<Trainer> findAllActive();
}
