package com.wrap.it.service;

public interface PasswordService {
    String generateCode(String email);

    boolean verifyCode(String email, String code);

    void removeCode(String email);
}
