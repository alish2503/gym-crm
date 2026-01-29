package com.gymcrm.presentation.controller;

import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTrainersDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Alish
 */
@Tag(name = "Trainees")
@RequestMapping(path = "/trainees", produces = "application/json")
public interface TraineeControllerApi {

    @Operation(summary = "Register a new trainee",
            description = "Creates a trainee account with generated username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content)
    })
    ResponseEntity<UserCredentialsDto> registerTrainee(CreateTraineeDto request);

    @Operation(summary = "Get trainee profile", description = "Fetch trainee info including assigned trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee found",
                    content = @Content(schema = @Schema(implementation = TraineeWithTrainersDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    TraineeWithTrainersDto getTraineeProfile(String username);

    @Operation(summary = "Update trainee profile", description = "Update trainee details like name, address, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = TraineeWithTrainersAfterUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    TraineeWithTrainersAfterUpdateDto updateTraineeProfile(String username, UpdateTraineeDto request);

    @Operation(summary = "Delete trainee profile", description = "Deletes trainee and associated trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    void deleteTraineeProfile(String username);

    @Operation(summary = "Get available trainers for trainee", description = "Fetch trainers that can be assigned to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available trainers",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainerDto.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    List<TrainerDto> getAvailableTrainers(String username);

    @Operation(summary = "Update assigned trainers for trainee", description = "Assign a list of trainers to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers updated successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainerDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer not found", content = @Content)
    })
    List<TrainerDto> updateTrainers(String username, UpdateTrainersDto updateTrainersDto);
}
