package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.PasswordService;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.application.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TraineeServiceImpl extends UserServiceImpl<Trainee> implements TraineeService {
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, PasswordService passwordService) {
        super(traineeRepository, passwordService);
        this.traineeRepository = traineeRepository;
    }

    @Override
    public void deleteTrainee(String username) {
        log.info("Deleting trainee with username: {}", username);
        traineeRepository.delete(username);
        log.debug("Trainee {} deleted", username);
    }
}

