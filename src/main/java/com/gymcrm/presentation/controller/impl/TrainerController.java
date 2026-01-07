package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.TrainerService;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.presentation.controller.port.TrainerControllerApi;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.UpdateTrainerDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TrainerWithTraineesDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import com.gymcrm.presentation.mapper.TrainerDtoMapper;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alish
 */
@RestController
public class TrainerController extends AbstractUserController  implements TrainerControllerApi {
    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService)
    {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCredentialsDto> registerTrainer(@RequestBody @Valid CreateTrainerDto request) {
        UserCredentials credentials = trainerService.createTrainer(TrainerDtoMapper.toCommand(request));
        return createUserCredentialsResponse(credentials, "trainers");
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public TrainerWithTraineesDto getTrainerProfile(@PathVariable String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return TrainerDtoMapper.toDtoWithTrainees(trainer);
    }

    @PutMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public TrainerWithTraineesAfterUpdateDto updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTrainerDto request
    ) {
        Trainer trainer = trainerService.updateTrainer(TrainerDtoMapper.toCommand(username, request));
        return TrainerDtoMapper.toDtoWithTraineesForUpdate(trainer);
    }
}