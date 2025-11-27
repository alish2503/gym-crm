package com.gymcrm.infrastructure.security.service.impl;

import com.gymcrm.infrastructure.security.service.port.TokenBlacklistService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alish
 */
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    @Override
    public void blacklist(String token, Date expiration) {
        blacklist.put(token, expiration.getTime());
    }

    @Override
    public boolean isBlacklisted(String token) {
        Long exp = blacklist.get(token);
        if (exp == null) return false;
        if (exp < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
