package com.gymcrm.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
@Setter
@Getter
public class Trainee extends UserProfile {
    private LocalDate dateOfBirth;
    private String address;
    private List<Trainer> trainers = new ArrayList<>();

    public Trainee(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(Long id, User user, LocalDate dateOfBirth, String address) {
        super(id, user);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(User user, LocalDate dateOfBirth, String address) {
        super(user);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(Long id, LocalDate dateOfBirth, String address) {
        super(id);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee() {}

}
