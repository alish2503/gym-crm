package com.gymcrm.dao;

import com.gymcrm.model.Trainee;

import java.util.List;

/**
 * @author Alish
 */
public interface TraineeDao {
    void save(Trainee trainee);
    void update(Trainee trainee);
    void delete(String username);
    Trainee findByUsername(String username);
    List<Trainee> findAll();
}
