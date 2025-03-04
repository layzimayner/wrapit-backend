package com.wrap.it.controller;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.model.User;
import com.wrap.it.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management", description = "Endpoints for management users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

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
}
