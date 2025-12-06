package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;
import java.util.Optional;

/**
 * @author Alish
 */
public interface TraineeRepository extends BaseRepository<Trainee> {
    void deleteTrainee(Trainee trainee);
    Optional<Trainee> findTraineeWithTrainers(String username);
    Optional<Trainee> findTrainee(String username);
    Optional<Long> findTraineeId(String username);
}
