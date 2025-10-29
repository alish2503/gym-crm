package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
@Entity
public class TraineeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<TrainerDao> trainers = new ArrayList<>();

    @OneToMany(mappedBy="trainee", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TrainingDao> trainings = new ArrayList<>();

    public TraineeDao(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public TraineeDao() {
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserDao getUser() {
        return user;
    }
}
