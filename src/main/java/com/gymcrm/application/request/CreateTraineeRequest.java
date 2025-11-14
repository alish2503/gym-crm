package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTraineeRequest extends FullName {
    private final LocalDate dateOfBirth;
    private final String address;

    public CreateTraineeRequest(String firstName, String lastName, LocalDate dateOfBirth, String address)
    {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
}
