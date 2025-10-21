package com.gymcrm.dao.impl;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@Repository
public class TraineeDaoImpl implements TraineeDao {
    private final Map<String, Trainee> traineeMap;

    @Autowired
    public TraineeDaoImpl(InMemoryStorage storage) {
        this.traineeMap = storage.getNamespace("trainees");
    }

    @Override
    public void save(Trainee trainee) {
        traineeMap.put(trainee.getUsername(), trainee);
    }

    @Override
    public void update(Trainee trainee) {
        traineeMap.put(trainee.getUsername(), trainee);
    }

    @Override
    public void delete(String username) {
        traineeMap.remove(username);
    }

    @Override
    public Trainee findByUsername(String username) {
        return traineeMap.get(username);
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }
}
