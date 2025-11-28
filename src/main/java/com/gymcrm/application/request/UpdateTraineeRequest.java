package com.gymcrm.application.request;

import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
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

}
