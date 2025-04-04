package com.wrap.it.controller;

import com.wrap.it.dto.user.GoogleRegisterDto;
import com.wrap.it.dto.user.ResetPasswordDto;
import com.wrap.it.dto.user.UserLoginRequestDto;
import com.wrap.it.dto.user.UserLoginResponseDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.security.AuthenticationService;
import com.wrap.it.service.CodeAttemptService;
import com.wrap.it.service.EmailService;
import com.wrap.it.service.PasswordResetService;
import com.wrap.it.service.RateLimitService;
import com.wrap.it.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final PasswordResetService passwordResetService;
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
        if (!rateLimitService.canSendCode(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests. Try again later.");
        }

        rateLimitService.registerRequest(email);

        String resetCode = passwordResetService.generateResetCode(email);

        emailService.sendResetCode(email, resetCode);

        return ResponseEntity.ok("Code sent to email");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Set new password", description = "Set new password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto request) {
        if (!codeAttemptService.canAttempt(request.email())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed attempts. Try again later.");
        }

        if (!passwordResetService.verifyResetCode(request.email(), request.code())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }

        userService.updatePassword(request.email(), request.newPassword());
        passwordResetService.removeResetCode(request.email());
        codeAttemptService.resetAttempts(request.email());

        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/google-login")
    @Operation(summary = "Google Account Authentication",
            description = "Authenticate users via Google for registration or login")
    public UserLoginResponseDto googleAuth(OAuth2AuthenticationToken token,
                                           @RequestBody GoogleRegisterDto request) {
        return userService.googleLogin(token.getPrincipal().getAttributes(), request);
    }

}
