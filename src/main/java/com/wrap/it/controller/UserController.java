package com.wrap.it.controller;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.model.User;
import com.wrap.it.service.CodeAttemptService;
import com.wrap.it.service.EmailService;
import com.wrap.it.service.PasswordResetService;
import com.wrap.it.service.RateLimitService;
import com.wrap.it.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management", description = "Endpoints for management users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final RateLimitService rateLimitService;
    private final CodeAttemptService codeAttemptService;

    @PutMapping("/{id}")
    @Operation(summary = "Update user's roles", description = "Update user's roles by id")
    public UserWithRoleDto updateRoles(
            @RequestBody @Valid RoleRequestDto requestDto,
            @PathVariable @Positive Long id) {
        return userService.updateRoles(requestDto, id);
    }

    @GetMapping
    @Operation(summary = "Get profile info", description = "Get profile info")
    public UserRegistrationDto getProfileInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.findInfo(user);
    }

    @PutMapping
    @Operation(summary = "Update profile info", description = "Update profile info")
    public UserRegistrationDto updateProfileInfo(
            @RequestBody @Valid UserRegistrationRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.updateProfile(user, requestDto);
    }

    @PostMapping("/send-verification-code")
    @Operation(summary = "Connect email", description = "Send verification code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam @Email String email,
                                                       Authentication authentication) {
        if (!rateLimitService.canSendCode(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests. Try again later.");
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email is already in use");
        }

        rateLimitService.registerRequest(email);

        User user = (User) authentication.getPrincipal();

        String verificationCode = passwordResetService.generateInitCode(email, user);
        emailService.sendResetCode(email, verificationCode);
        return ResponseEntity.ok("Confirmation code sent");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email,
                                              @RequestParam String code,
                                              Authentication authentication) {
        if (!codeAttemptService.canAttempt(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed attempts. Try again later.");
        }

        User user = (User) authentication.getPrincipal();

        if (!passwordResetService.verifyInitCode(email, code, user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }

        userService.updateEmail(email, user);
        passwordResetService.removeResetCode(email);

        codeAttemptService.resetAttempts(email);

        return ResponseEntity.ok("Email successfully verified and linked to account");
    }
}
