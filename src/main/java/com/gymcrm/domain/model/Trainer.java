package com.gymcrm.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class Trainer extends UserProfile {
    private TrainingType specialization;
    private List<Trainee> trainees = new ArrayList<>();

    public Trainer(Long id, User user, TrainingType specialization) {
        super(id, user);
        this.specialization = specialization;
    }

    public Trainer(User user, TrainingType specialization) {
        super(user);
        this.specialization = specialization;
    }

    public Trainer(Long id, TrainingType specialization) {
        super(id);
        this.specialization = specialization;
    }

    public Trainer(TrainingType specialization) {
        this.specialization = specialization;
    }

    public Trainer() {}

    public TrainingType getSpecialization() {
        return specialization;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }
}
