package com.wrap.it.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank
        String phoneNumber,
        @NotBlank
        String password
) {
}
