package com.gymcrm.dao.impl;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Training;
import com.gymcrm.storage.InMemoryStorage;
import org.springframework.stereotype.Repository;

/**
 * @author Alish
 */
@Repository
public class TrainingDaoImpl extends BaseDaoImpl<Training, Long> implements TrainingDao {

    private long idCounter = 0;

    public TrainingDaoImpl(InMemoryStorage storage) {
        super(storage, "trainings");

        if (!storageMap.isEmpty()) {
            idCounter = storageMap.keySet().stream()
                    .mapToLong(id -> (Long) id)
                    .max()
                    .orElse(0L);
        }
    }

    @Override
    public Training save(Training training) {
        if (training.getId() == null) {
            training.setId(++idCounter);
        }
        storageMap.put(training.getId(), training);
        return training;
    }

    @Override
    public Training findById(Long id) {
        return storageMap.get(id);
    }
}

