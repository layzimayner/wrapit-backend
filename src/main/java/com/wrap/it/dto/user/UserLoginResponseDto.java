package com.wrap.it.dto.user;

public record UserLoginResponseDto(String token,
                                   Long userId,
                                   String firstName,
                                   String lastName,
                                   String phoneNumber) {
}
