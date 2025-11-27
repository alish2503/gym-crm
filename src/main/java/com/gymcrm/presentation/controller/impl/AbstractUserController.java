package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.UserCredentialsDtoMapper;
import org.springframework.http.ResponseEntity;

import java.net.URI;

abstract class AbstractUserController {
    protected ResponseEntity<UserCredentialsDto> createUserCredentialsResponse(UserCredentials credentials, String pathName) {
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/" + pathName + "/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }
}
