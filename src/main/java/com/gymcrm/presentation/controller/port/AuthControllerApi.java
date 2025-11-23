package com.gymcrm.presentation.controller.port;

import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.LoginDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author Alish
 */
@Tag(name= "Authentication")
@RequestMapping(path = "/auth", produces = "application/json")
public interface AuthControllerApi {

    @Operation(summary = "Register a new trainee",
            description = "Creates a trainee account with generated username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    ResponseEntity<UserCredentialsDto> registerTrainee(CreateTraineeDto request);

    @Operation(summary = "Register a new trainer",
            description = "Creates a trainer account with generated username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer created successfully",
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    ResponseEntity<UserCredentialsDto> registerTrainer(CreateTrainerDto request);

    @Operation(summary = "Login and get JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login succeeded", content = @Content(
                    examples = @ExampleObject(
                            value = "{ \"accessToken\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}"
                    )
            )),
            @ApiResponse(responseCode = "401", description = "Wrong username or password", content = @Content),
            @ApiResponse(responseCode = "429", description = "User blocked temporary", content = @Content)
    })
    ResponseEntity<Map<String, String>> login(LoginDto loginDto);

    @Operation(summary = "Invalidate current token (logout)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout succeeded"),
            @ApiResponse(responseCode = "401", description = "Invalid token or not provided"),
    })
    ResponseEntity<Void> logout(@Parameter(hidden = true) String authHeader);
}
