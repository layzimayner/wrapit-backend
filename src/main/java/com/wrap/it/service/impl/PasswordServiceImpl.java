package com.wrap.it.service.impl;

import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.model.PasswordResetToken;
import com.wrap.it.repository.PasswordResetTokenRepository;
import com.wrap.it.repository.UserRepository;
import com.wrap.it.service.PasswordService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void removeCode(String code) {
        tokenRepository.findByCode(code).ifPresent(tokenRepository::delete);
    }

    @Override
    @Transactional
    public String generateCode(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        tokenRepository.deleteAllByEmail(email);

        PasswordResetToken token = new PasswordResetToken();
        token.setCode(code);
        token.setEmail(email);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        tokenRepository.save(token);

        return code;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        PasswordResetToken token = tokenRepository.findByCodeAndEmail(code, email).orElseThrow(() ->
                new EntityNotFoundException("Token with email: " + email
                    + " and code: " + code + " doesn't exist"));
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }
}
