package com.wrap.it.service;

public interface EmailService {
    void sendResetCode(String email, String code, String str);
}
