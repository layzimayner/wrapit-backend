package com.wrap.it.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserInfoDto(@NotBlank String firstName,
                                @NotBlank String lastName,
                                String middleName,
                                String shippingAddress) {
}
