package com.gymcrm.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
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
}
