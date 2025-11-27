package com.gymcrm.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Alish
 */
public record UserCredentialsDto(

        @Schema(example = "John.Doe")
        String username,

        @Schema(example = "pass")
        String password
) {}
