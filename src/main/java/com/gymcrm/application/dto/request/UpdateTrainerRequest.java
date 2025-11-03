package com.gymcrm.application.dto.request;

/**
 * @author Alish
 */
public class UpdateTrainerRequest extends CreateTrainerRequest {
    private String userName;
    private boolean isActive;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
