package com.gymcrm.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Alish
 */
public class LoginDto {

    @Schema(example = "John.Doe")
    @NotBlank(message = "Username required")
    private String username;

    @Schema(example = "password")
    @NotBlank(message = "Password required")
    private String password;

    public LoginDto() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
