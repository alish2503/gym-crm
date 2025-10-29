package com.gymcrm.domain.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(String userName, String password, String firstName, String lastName, boolean isActive,
                   LocalDate dateOfBirth, String address) {

        super(userName, password, firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(String firstName, String lastName, boolean isActive, LocalDate dateOfBirth, String address) {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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
}
