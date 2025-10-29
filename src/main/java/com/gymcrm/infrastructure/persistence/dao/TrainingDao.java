package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Entity
public class TrainingDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String trainingName;

    @Column(nullable=false)
    private LocalDate trainingDate;

    @Column(nullable=false)
    private Integer duration; // minutes

    @ManyToOne(optional=false)
    @JoinColumn(name="trainee_id")
    private TraineeDao trainee;

    @ManyToOne(optional=false)
    @JoinColumn(name="trainer_id")
    private TrainerDao trainer;

    @ManyToOne(optional=false)
    @JoinColumn(name="training_type_id")
    TrainingTypeDao trainingType;

    public TrainingDao() {}

    public TrainingDao(String trainingName, LocalDate trainingDate, Integer duration, TraineeDao trainee, TrainerDao trainer, TrainingTypeDao trainingType) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingType = trainingType;
    }

    public Long getId() {
        return id;
    }


    public String getTrainingName() {
        return trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public TraineeDao getTrainee() {
        return trainee;
    }

    public TrainerDao getTrainer() {
        return trainer;
    }
}
