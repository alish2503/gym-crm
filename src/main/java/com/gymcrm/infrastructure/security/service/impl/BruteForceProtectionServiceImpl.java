package com.gymcrm.infrastructure.security.service.impl;

import com.gymcrm.infrastructure.security.service.BruteForceProtectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alish
 */
@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {
    private final int maxAttempts;
    private final long blockMillis;
    private final Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    private static class AttemptInfo {
        int attempts;
        long blockedUntil;
    }

    public BruteForceProtectionServiceImpl(@Value("${security.login.max-attempts}") int maxAttempts,
                                           @Value("${security.login.block-minutes}") long blockMinutes)
    {
        this.maxAttempts = maxAttempts;
        this.blockMillis = blockMinutes * 60 * 1000;
    }

    @Override
    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    @Override
    public void loginFailed(String username) {
        AttemptInfo info = attemptsCache.computeIfAbsent(username, k -> new AttemptInfo());
        if (++info.attempts >= maxAttempts) {
            info.blockedUntil = now() + blockMillis;
        }
    }

    public long checkBlocked(String username) {
        AttemptInfo info = attemptsCache.get(username);
        if (info == null) return 0;
        long remaining = Math.max(0, info.blockedUntil - now());
        if (info.attempts >= maxAttempts && remaining == 0) {
            attemptsCache.remove(username);
        }
        return remaining;
    }

    protected long now() {
        return System.currentTimeMillis();
    }
}

