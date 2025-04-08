package com.wrap.it.service;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.ChangePasswordRequest;
import com.wrap.it.dto.user.ResetPasswordDto;
import com.wrap.it.dto.user.UpdateUserInfoDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserRegistrationDto save(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserWithRoleDto updateRoles(RoleRequestDto requestDto, Long id);

    UserRegistrationDto findInfo(User user);

    UserRegistrationDto updateProfile(User user, UpdateUserInfoDto requestDto);

    ResponseEntity<String> sendConnectCode(User user, String email);

    ResponseEntity<String> verify(String email, String code, User user);

    ResponseEntity<String> sendResetPassword(String email);

    ResponseEntity<String> resetPassword(ResetPasswordDto request);

    ResponseEntity<String> changePassword(User user, ChangePasswordRequest request);
}
