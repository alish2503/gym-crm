package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainer;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TrainerRepository extends BaseRepository<Trainer> {
    Optional<Trainer> findTrainerWithTrainees(String username);
    Optional<Trainer> findTrainer(String username);
    List<Trainer> findAvailableTrainersNotAssignedAndActive(String traineeUsername);
    List<Trainer> findTrainersByUserNamesIn(List<String> usernames);
}
