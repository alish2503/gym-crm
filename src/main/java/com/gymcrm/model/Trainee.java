package com.gymcrm.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;


    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
