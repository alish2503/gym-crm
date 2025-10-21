package com.gymcrm.dao;

import com.gymcrm.model.Trainer;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainerDao {
    void save(Trainer trainer);
    void update(Trainer trainer);
    Trainer findByUsername(String username);
    List<Trainer> findAll();
}
