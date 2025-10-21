package com.gymcrm.dao;

import com.gymcrm.model.Training;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainingDao {
    Training save(Training training);
    Training findById(Long id);
    List<Training> findAll();
}
