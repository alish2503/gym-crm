package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Alish
 */
@Getter
public class CreateTraineeRequest extends FullName {
    private final LocalDate dateOfBirth;
    private final String address;

    public CreateTraineeRequest(String firstName, String lastName, LocalDate dateOfBirth, String address)
    {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

}
