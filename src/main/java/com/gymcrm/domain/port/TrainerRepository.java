package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainerRepository extends UserRepository<Trainer> {
    Optional<Trainer> findTrainerWithTrainees(String userName);
    List<Trainer> getAvailableTrainersNotAssigned(List<Long> assignedIds);
    List<Trainer> findTrainersByUserNamesIn(List<String> userNames);
    List<Long> findAssignedTrainersIds(String traineeUserName);
    List<Trainer> findAll();
}
