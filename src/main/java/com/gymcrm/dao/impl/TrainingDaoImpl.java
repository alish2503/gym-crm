package com.gymcrm.dao.impl;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Training;
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
public class TrainingDaoImpl implements TrainingDao {
    private final Map<Long, Training> trainingMap;
    private long idCounter = 0;

    @Autowired
    public TrainingDaoImpl(InMemoryStorage storage) {
        this.trainingMap = storage.getNamespace("trainings");
        if (!trainingMap.isEmpty()) {
            idCounter = trainingMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
        }
    }

    @Override
    public Training save(Training training) {
        if (training.getId() == null) {
            training.setId(++idCounter);
        }
        trainingMap.put(training.getId(), training);
        return training;
    }

    @Override
    public Training findById(Long id) {
        return trainingMap.get(id);
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }
}

