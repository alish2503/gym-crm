package com.gymcrm.infrastructure.persistence.dao;

/**
 * @author Alish
 */
public class TrainingTypeDao {
    Long id;
    private final String name;

    public TrainingTypeDao(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TrainingTypeDao(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}