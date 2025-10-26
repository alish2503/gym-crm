package com.gymcrm.service;

import com.gymcrm.application.service.PasswordService;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.application.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TrainerServiceImpl extends UserServiceImpl<Trainer> implements TrainerService {

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, PasswordService passwordService) {
        super(trainerRepository, passwordService);
    }
}

