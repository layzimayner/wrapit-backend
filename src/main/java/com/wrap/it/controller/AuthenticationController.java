package com.wrap.it.controller;

import com.wrap.it.dto.user.ResetPasswordDto;
import com.wrap.it.dto.user.UserLoginRequestDto;
import com.wrap.it.dto.user.UserLoginResponseDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.security.AuthenticationService;
import com.wrap.it.service.CodeAttemptService;
import com.wrap.it.service.EmailService;
import com.wrap.it.service.PasswordService;
import com.wrap.it.service.RateLimitService;
import com.wrap.it.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users authorization management",
        description = "Endpoints for management authorisation")
@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final PasswordService passwordResetService;
    private final EmailService emailService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RateLimitService rateLimitService;
    private final CodeAttemptService codeAttemptService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Return JWT and user "
            + "info after successful authorization")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @Operation(summary = "Registration", description = "Add new user to DB")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.save(requestDto);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Reset password", description = "Send verification code on email")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return userService.sendResetPassword(email);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Set new password", description = "Set new password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto request) {
        return userService.resetPassword(request);
    }
}
