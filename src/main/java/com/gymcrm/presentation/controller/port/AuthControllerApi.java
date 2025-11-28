package com.gymcrm.presentation.controller.port;

import com.gymcrm.presentation.dto.request.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    void logout(@Parameter(hidden = true) String authHeader);
}
