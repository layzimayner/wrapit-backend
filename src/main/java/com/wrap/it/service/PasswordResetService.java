package com.wrap.it.service;

import com.wrap.it.model.User;

public interface PasswordResetService {
    String generateResetCode(String email);

    boolean verifyResetCode(String email, String code);

    boolean verifyInitCode(String email, String code, User user);

    void removeResetCode(String email);

    String generateInitCode(String email, User user);
}
