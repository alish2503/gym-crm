package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainerRepository extends UserRepository<Trainer> {
    List<Trainer> getAvailableTrainersNotAssigned(Trainee trainee);
    List<Trainer> findAllByUserNameIn(List<String> userNames);
}
