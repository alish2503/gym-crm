package com.gymcrm.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Trainee{username='%s', firstName='%s', lastName='%s'}",
                getUsername(), getFirstName(), getLastName());
    }

}
