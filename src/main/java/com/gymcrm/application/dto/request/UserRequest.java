package com.gymcrm.application.dto.request;

/**
 * @author Alish
 */
public class UserRequest {
    String userName;
    String newPassword;

    public UserRequest(String userName, String newPassword) {
        this.userName = userName;
        this.newPassword = newPassword;
    }
}
