package com.gymcrm.dao.impl;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Alish
 */
@Repository
public class TraineeDaoImpl extends UserDaoImpl<Trainee> implements TraineeDao {

    @Autowired
    public TraineeDaoImpl(InMemoryStorage storage) {
        super(storage, "trainees");
    }

    @Override
    public void delete(String username) {
        storageMap.remove(username);
    }
}
