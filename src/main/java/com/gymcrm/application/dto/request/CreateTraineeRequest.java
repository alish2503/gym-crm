package com.gymcrm.application.dto.request;

import com.gymcrm.domain.model.FullName;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTraineeRequest extends FullName {
    private LocalDate dateOfBirth;
    private String address;

    public CreateTraineeRequest(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public CreateTraineeRequest() {}

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
