package com.wrap.it.service.impl;

import com.wrap.it.dto.role.RoleRequestDto;
import com.wrap.it.dto.user.ChangePasswordRequest;
import com.wrap.it.dto.user.ResetPasswordDto;
import com.wrap.it.dto.user.UpdateUserInfoDto;
import com.wrap.it.dto.user.UserRegistrationDto;
import com.wrap.it.dto.user.UserRegistrationRequestDto;
import com.wrap.it.dto.user.UserWithRoleDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.exception.RegistrationException;
import com.wrap.it.mapper.UserMapper;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import com.wrap.it.repository.RoleRepository;
import com.wrap.it.repository.UserRepository;
import com.wrap.it.service.CartService;
import com.wrap.it.service.CodeAttemptService;
import com.wrap.it.service.EmailService;
import com.wrap.it.service.PasswordService;
import com.wrap.it.service.RateLimitService;
import com.wrap.it.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final RateLimitService rateLimitService;
    private final CodeAttemptService codeAttemptService;

    @Override
    @Transactional
    public UserRegistrationDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Email is already in use");
        }

        if (!rateLimitService.canSendCode(requestDto.getEmail())) {
            throw new RegistrationException("Too many failed attempts. Try again later.");
        }

        rateLimitService.registerRequest(requestDto.getEmail());

        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.USER)));

        String code = passwordService.generateCode(requestDto.getEmail());

        user.setPassword(passwordEncoder.encode(code));
        userRepository.save(user);

        emailService.sendResetCode(requestDto.getEmail(), code,
                "Use this code to login: ");

        cartService.createShoppingCartForUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserWithRoleDto updateRoles(RoleRequestDto requestDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update user with id "
                        + id + " because it does not exist")
        );
        user.setRoles(roleRepository.findRolesByIds(requestDto.rolesIds()));
        userRepository.save(user);
        return userMapper.toModelWithRoles(user);
    }

    @Override
    public UserRegistrationDto findInfo(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public UserRegistrationDto updateProfile(User user, UpdateUserInfoDto requestDto) {
        userMapper.update(user, requestDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public ResponseEntity<String> sendConnectCode(User user, String email) {
        if (!rateLimitService.canSendCode(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests. Try again later.");
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email is already in use");
        }

        rateLimitService.registerRequest(email);

        String verificationCode = passwordService.generateCode(email);

        emailService.sendResetCode(email, verificationCode, "Your reset code: ");

        return ResponseEntity.ok("Code sent to email. Please also check y"
                + "our Spam or Junk folder in case the email was filtered.");
    }

    @Override
    public ResponseEntity<String> verify(String email, String code, User user) {
        if (!codeAttemptService.canAttempt(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed attempts. Try again later.");
        }

        if (!passwordService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }

        user.setEmail(email);
        userRepository.save(user);

        passwordService.removeCode(email);

        codeAttemptService.resetAttempts(email);

        return ResponseEntity.ok("Email successfully verified and linked to account");
    }

    @Override
    public ResponseEntity<String> sendResetPassword(String email) {
        if (!rateLimitService.canSendCode(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many requests. Try again later.");
        }

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with email " + email
                        + " not found");
        }
        rateLimitService.registerRequest(email);

        String resetCode = passwordService.generateCode(email);

        emailService.sendResetCode(email, resetCode, "Use this code to reset password: ");

        return ResponseEntity.ok("Code sent to email. Please also check y"
                + "our Spam or Junk folder in case the email was filtered.");
    }

    @Override
    public ResponseEntity<String> resetPassword(ResetPasswordDto request) {
        if (!codeAttemptService.canAttempt(request.email())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed attempts. Try again later.");
        }

        if (!passwordService.verifyCode(request.email(), request.code())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code");
        }

        if (userRepository.findByEmail(request.email()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with email " + request.email()
                            + " not found");
        }
        User user = userRepository.findByEmail(request.email()).orElseThrow(() ->
                new EntityNotFoundException("User with email " + request.email()
                    + " don't exist"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setOriginalPassword(true);

        passwordService.removeCode(request.email());
        codeAttemptService.resetAttempts(request.email());

        return ResponseEntity.ok("Password changed successfully");
    }

    @Override
    public ResponseEntity<String> changePassword(User user, ChangePasswordRequest request) {
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setOriginalPassword(true);

        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }
}
