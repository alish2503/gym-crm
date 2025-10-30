package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
@Entity
@Table(name = "trainers")
public class TrainerDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TrainingTypeDao specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    List<TraineeDao> trainees = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<TrainingDao> trainings = new ArrayList<>();

    public TrainerDao() {
    }

    public TrainerDao(TrainingTypeDao specialization) {
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public UserDao getUser() {
        return user;
    }

    public TrainingTypeDao getSpecialization() {
        return specialization;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }
}
