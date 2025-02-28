package com.wrap.it.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        String phoneNumber,
        @NotBlank
        String password
) {
}
