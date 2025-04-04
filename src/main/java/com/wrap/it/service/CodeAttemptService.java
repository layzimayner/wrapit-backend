package com.wrap.it.service;

public interface CodeAttemptService {
    boolean canAttempt(String email);

    void resetAttempts(String email);
}
