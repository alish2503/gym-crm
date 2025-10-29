package com.gymcrm.domain.model;

/**
 * @author Alish
 */
public class TrainingType {

    private final TrainingTypeEnum name;

    public TrainingType(TrainingTypeEnum name) {
        this.name = name;
    }

    public TrainingTypeEnum getName() {
        return name;
    }

}