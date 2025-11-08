package com.gymcrm.application.request;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTraineeRequest extends CreateUserRequest {
    private final LocalDate dateOfBirth;
    private final String address;

    public CreateTraineeRequest(boolean isActive, String firstName, String lastName, LocalDate dateOfBirth,
                                String address)
    {
        super(isActive, firstName, lastName);
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
