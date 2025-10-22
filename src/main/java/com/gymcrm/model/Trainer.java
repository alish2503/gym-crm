package com.gymcrm.model;

/**
 * @author Alish
 */
public class Trainer extends User {
    private TrainingType specialization;

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
}
