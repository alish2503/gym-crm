package com.gymcrm.dao.impl;

import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainer;
import com.gymcrm.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

/**
 * @author Alish
 */
@Repository
public class TrainerDaoImpl extends UserDaoImpl<Trainer> implements TrainerDao {

    public TrainerDaoImpl(InMemoryStorage storage) {
        super(storage, "trainers");
    }
}
