package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
public class LoginDto {

    @Schema(example = "John.Doe")
    @NotBlank(message = "Username required")
    private String username;

    @Schema(example = "password")
    @NotBlank(message = "Password required")
    private String password;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
