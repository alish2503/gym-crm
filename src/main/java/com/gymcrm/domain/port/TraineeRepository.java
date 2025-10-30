package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Training;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface TraineeRepository extends UserRepository<Trainee> {
    void delete(String username);
}
