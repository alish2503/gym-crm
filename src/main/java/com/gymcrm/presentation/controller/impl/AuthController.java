package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.AuthService;
import com.gymcrm.application.service.port.TraineeService;
import com.gymcrm.application.service.port.TrainerService;
import com.gymcrm.presentation.controller.port.AuthControllerApi;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.LoginDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.TraineeDtoMapper;
import com.gymcrm.presentation.mapper.TrainerDtoMapper;
import com.gymcrm.presentation.mapper.UserCredentialsDtoMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

/**
 * @author Alish
 */
@RestController
public class AuthController implements AuthControllerApi {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthService authService;

    @Autowired
    public AuthController(TraineeService traineeService, TrainerService trainerService,
                          AuthService authService)
    {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.authService = authService;
    }

    @PostMapping("/register/trainees")
    public ResponseEntity<UserCredentialsDto> registerTrainee(@RequestBody @Valid CreateTraineeDto request) {
        UserCredentials credentials = traineeService.createTrainee(TraineeDtoMapper.toDomain(request));
        return createUserCredentialsResponse(credentials, "trainees");
    }

    @PostMapping("/register/trainers")
    public ResponseEntity<UserCredentialsDto> registerTrainer(@RequestBody @Valid CreateTrainerDto request) {
        UserCredentials credentials = trainerService.createTrainer(TrainerDtoMapper.toDomain(request));
        return createUserCredentialsResponse(credentials, "trainers");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDto loginDto) {
        String token = authService.login(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok().body(Map.of("accessToken", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false)
                                           String authHeader)
    {
        authService.logout(authHeader);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<UserCredentialsDto> createUserCredentialsResponse(UserCredentials credentials,
                                                                             String pathName)
    {
        UserCredentialsDto credentialsDto = UserCredentialsDtoMapper.toDto(credentials);
        URI location = URI.create("/" + pathName + "/" + credentialsDto.username());
        return ResponseEntity.created(location).body(credentialsDto);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, String>> handleLocked(LockedException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleLogout(InsufficientAuthenticationException ex) {
        log.warn("Logout failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }
}
