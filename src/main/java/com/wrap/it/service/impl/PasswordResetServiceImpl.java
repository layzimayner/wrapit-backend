package com.wrap.it.service.impl;

import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.model.PasswordResetToken;
import com.wrap.it.model.User;
import com.wrap.it.repository.PasswordResetTokenRepository;
import com.wrap.it.repository.UserRepository;
import com.wrap.it.service.PasswordResetService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String generateResetCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User with email: " + email
                + " doesn't exist"));

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        tokenRepository.deleteByUser(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setCode(code);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        tokenRepository.save(token);

        return code;
    }

    @Override
    public void removeResetCode(String code) {
        tokenRepository.findByCode(code).ifPresent(tokenRepository::delete);
    }

    @Override
    @Transactional
    public String generateInitCode(String email, User user) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("Email already in use");
        }

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        tokenRepository.deleteByUser(user);

        PasswordResetToken token = new PasswordResetToken();
        token.setCode(code);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        tokenRepository.save(token);

        return code;
    }

    @Override
    public boolean verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User with email: " + email
                        + " doesn't exist"));

        PasswordResetToken token = tokenRepository.findByCodeAndUser(code, user).orElseThrow(() ->
                new EntityNotFoundException("Token with email: " + email
                    + " and code: " + code + " doesn't exist"));
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean verifyInitCode(String email, String code, User user) {
        PasswordResetToken token = tokenRepository.findByCodeAndUser(code, user).orElseThrow(() ->
                new EntityNotFoundException("Token with email: " + email
                        + " and code: " + code + " doesn't exist"));
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }
}
