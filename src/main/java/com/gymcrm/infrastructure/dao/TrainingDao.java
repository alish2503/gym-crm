package com.gymcrm.infrastructure.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Entity
@Table(name = "training",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"trainer_id", "trainee_id", "training_date", "name"}
        ))

public class TrainingDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, name = "training_date")
    private LocalDate date;

    @Column(nullable=false)
    private int duration;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="trainee_id", nullable = false)
    private TraineeDao trainee;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="trainer_id", nullable = false)
    private TrainerDao trainer;

    @ManyToOne(optional=false, fetch = FetchType.EAGER)
    @JoinColumn(name="training_type_id", nullable = false)
    TrainingTypeDao type;

    public TrainingDao() {}

    public TrainingDao(String name, LocalDate date, Integer duration, TraineeDao trainee,
                       TrainerDao trainer, TrainingTypeDao type) {

        this.name = name;
        this.date = date;
        this.duration = duration;
        this.trainee = trainee;
        this.trainer = trainer;
        this.type = type;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public TraineeDao getTrainee() {
        return trainee;
    }

    public TrainerDao getTrainer() {
        return trainer;
    }

    public TrainingTypeDao getType() {
        return type;
    }
}
