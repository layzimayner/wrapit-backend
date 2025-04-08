package com.wrap.it.dto.user;

import com.wrap.it.annotation.PasswordMatches;

@PasswordMatches
public record ChangePasswordRequest(String password, String repeatPassword) {
}
