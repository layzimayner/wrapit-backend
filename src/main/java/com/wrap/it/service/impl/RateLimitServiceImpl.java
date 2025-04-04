package com.wrap.it.service.impl;

import com.wrap.it.service.RateLimitService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class RateLimitServiceImpl implements RateLimitService {
    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 10;

    private final Map<String, UserRequestInfo> requestCache = new ConcurrentHashMap<>();

    public boolean canSendCode(String email) {
        UserRequestInfo info = requestCache.getOrDefault(email,
                new UserRequestInfo(0, LocalDateTime.now()));

        return info.attempts < MAX_ATTEMPTS || !info.lastAttempt
                .plusMinutes(BLOCK_DURATION_MINUTES).isAfter(LocalDateTime.now());
    }

    public void registerRequest(String email) {
        UserRequestInfo info = requestCache.getOrDefault(email, new UserRequestInfo(0,
                LocalDateTime.now()));

        if (info.lastAttempt.plusMinutes(BLOCK_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            info.attempts = 0;
        }

        info.attempts++;
        info.lastAttempt = LocalDateTime.now();
        requestCache.put(email, info);
    }

    private static class UserRequestInfo {
        private int attempts;
        private LocalDateTime lastAttempt;

        public UserRequestInfo(int attempts, LocalDateTime lastAttempt) {
            this.attempts = attempts;
            this.lastAttempt = lastAttempt;
        }
    }
}
