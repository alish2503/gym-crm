package com.gymcrm.model;

/**
 * @author Alish
 */
public class Trainer extends User {
    private TrainingType specialization;

    public Trainer(String firstName, String lastName, TrainingType specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }
}
