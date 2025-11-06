package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
@Entity
@Table(name = "trainees")
public class TraineeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<TrainerDao> trainers = new ArrayList<>();

    @OneToMany(mappedBy="trainee", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    private List<TrainingDao> trainings = new ArrayList<>();

    public TraineeDao() {
    }

    public TraineeDao(Long id, UserDao user, LocalDate dateOfBirth, String address) {
        this.id = id;
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public TraineeDao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }

    public List<TrainerDao> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<TrainerDao> trainers) {
        this.trainers = trainers;
    }
}
