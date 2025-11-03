package com.gymcrm.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class Trainer {
    private Long id;
    private User user;
    private TrainingType specialization;
    private List<Trainee> trainees = new ArrayList<>();

    public Trainer(Long id, User user, TrainingType specialization) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }
}
