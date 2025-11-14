package com.gymcrm.application.request;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class UpdateTraineeRequest extends UpdateUserRequest {
    private final LocalDate dateOfBirth;
    private final String address;

    public UpdateTraineeRequest(String username, String firstName, String lastName,
                                boolean isActive, LocalDate dateOfBirth, String address)
    {
        super(username, firstName, lastName, isActive);
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
