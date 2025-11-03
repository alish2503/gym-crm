package com.gymcrm.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class Trainee {
    private Long id;
    private User user;
    private LocalDate dateOfBirth;
    private String address;
    private List<Trainer> trainers = new ArrayList<>();

    public Trainee(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(Long id, User user, LocalDate dateOfBirth, String address) {
        this.id = id;
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }
}
