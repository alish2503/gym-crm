package com.gymcrm.domain.model;

/**
 * @author Alish
 */
public class Trainer extends User {
    private TrainingType specialization;

    public Trainer(String userName, String password, String firstName, String lastName,
                   boolean isActive, TrainingType specialization) {

        super(userName, password, firstName, lastName, isActive);
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName, boolean isActive, TrainingType specialization) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
    }

    public Trainer(TrainingType specialization) {
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
}
