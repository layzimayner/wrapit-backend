package com.wrap.it.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public abstract class PasswordAwareRequest {
    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;
}
