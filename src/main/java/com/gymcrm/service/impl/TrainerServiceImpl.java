package com.gymcrm.service.impl;

import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainer;
import com.gymcrm.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TrainerServiceImpl extends UserServiceImpl<Trainer> implements TrainerService {

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao) {
        super(trainerDao);
    }
}

