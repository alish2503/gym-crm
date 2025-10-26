package com.gymcrm.infrastructure.persistence.dao;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TraineeDao extends UserDao {
    private LocalDate dateOfBirth;
    private String address;


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
