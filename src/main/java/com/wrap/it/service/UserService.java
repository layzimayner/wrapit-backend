package com.wrap.it.service;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.GoogleRegisterDto;
import com.wrap.it.dto.user.UserLoginResponseDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.model.User;
import java.util.Map;

public interface UserService {
    UserRegistrationDto save(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserWithRoleDto updateRoles(RoleRequestDto requestDto, Long id);

    UserRegistrationDto findInfo(User user);

    UserRegistrationDto updateProfile(User user, UserRegistrationRequestDto requestDto);

    boolean existsByEmail(String email);

    void updatePassword(String email, String newPassword);

    void updateEmail(String email, User user);

    UserLoginResponseDto googleLogin(Map<String, Object> attributes, GoogleRegisterDto request);
}
