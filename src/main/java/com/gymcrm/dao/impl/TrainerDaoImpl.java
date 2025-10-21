package com.gymcrm.dao.impl;

import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainer;
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
public class TrainerDaoImpl implements TrainerDao {
    private final Map<String, Trainer> trainerMap;

    @Autowired
    public TrainerDaoImpl(InMemoryStorage storage) {
        this.trainerMap = storage.getNamespace("trainers");
    }

    @Override
    public void save(Trainer trainer) {
        trainerMap.put(trainer.getUsername(), trainer);
    }

    @Override
    public void update(Trainer trainer) {
        trainerMap.put(trainer.getUsername(), trainer);
    }

    @Override
    public Trainer findByUsername(String username) {
        return trainerMap.get(username);
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }
}
