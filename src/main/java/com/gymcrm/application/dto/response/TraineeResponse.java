package com.gymcrm.application.dto.response;

import com.gymcrm.domain.model.FullName;

/**
 * @author Alish
 */
public class TraineeResponse extends FullName {
    private String userName;

    public TraineeResponse(String firstName, String lastName, String userName) {
        super(firstName, lastName);
        this.userName = userName;
    }
}
