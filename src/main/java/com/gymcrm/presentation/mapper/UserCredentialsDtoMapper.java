package com.gymcrm.presentation.mapper;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;

/**
 * @author Alish
 */
public class UserCredentialsDtoMapper {

    private UserCredentialsDtoMapper() {}

    public static UserCredentialsDto toDto(UserCredentials credentials) {
        return new UserCredentialsDto(credentials.username(), credentials.password());
    }
}
