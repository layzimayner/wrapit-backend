package com.wrap.it.service.impl;

import com.wrap.it.service.CodeAttemptService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CodeAttemptServiceImpl implements CodeAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 15;

    private final Map<String, AttemptInfo> attemptCache = new ConcurrentHashMap<>();

    public boolean canAttempt(String email) {
        AttemptInfo info = attemptCache.getOrDefault(email, new AttemptInfo(0,
                LocalDateTime.now()));

        return info.attempts < MAX_ATTEMPTS || !info.lastAttempt
                .plusMinutes(BLOCK_DURATION_MINUTES).isAfter(LocalDateTime.now());
    }

    public void registerFailedAttempt(String email) {
        AttemptInfo info = attemptCache.getOrDefault(email, new AttemptInfo(0,
                LocalDateTime.now()));

        if (info.lastAttempt.plusMinutes(BLOCK_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            info.attempts = 0;
        }

        info.attempts++;
        info.lastAttempt = LocalDateTime.now();
        attemptCache.put(email, info);
    }

    public void resetAttempts(String email) {
        attemptCache.remove(email);
    }

    private static class AttemptInfo {
        private int attempts;
        private LocalDateTime lastAttempt;

        public AttemptInfo(int attempts, LocalDateTime lastAttempt) {
            this.attempts = attempts;
            this.lastAttempt = lastAttempt;
        }
    }
}
