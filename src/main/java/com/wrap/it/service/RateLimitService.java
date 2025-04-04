package com.wrap.it.service;

public interface RateLimitService {
    boolean canSendCode(String email);

    void registerRequest(String email);
}
