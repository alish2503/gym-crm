package com.gymcrm.infrastructure.service;

import com.gymcrm.infrastructure.security.service.impl.TokenBlacklistServiceImpl;
import com.gymcrm.infrastructure.security.service.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alish
 */

class TokenBlacklistServiceImplTest {

    @Test
    void blacklist_addsToken() {
        TokenBlacklistService svc = new TokenBlacklistServiceImpl();
        String token = "abc";
        Date exp = new Date(System.currentTimeMillis() + 1000);
        svc.blacklist(token, exp);
        assertTrue(svc.isBlacklisted(token));
    }

    @Test
    void notBlacklisted_whenNotAdded() {
        TokenBlacklistService svc = new TokenBlacklistServiceImpl();
        assertFalse(svc.isBlacklisted("unknown"));
    }

    @Test
    void expiredToken_isRemoved() throws InterruptedException {
        TokenBlacklistService svc = new TokenBlacklistServiceImpl();
        String token = "expired";
        Date shortExp = new Date(System.currentTimeMillis() + 50);
        svc.blacklist(token, shortExp);
        Thread.sleep(70);
        assertFalse(svc.isBlacklisted(token));
    }

    @Test
    void notExpired_staysBlacklisted() {
        TokenBlacklistService svc = new TokenBlacklistServiceImpl();
        String token = "valid";
        Date future = new Date(System.currentTimeMillis() + 5000);
        svc.blacklist(token, future);
        assertTrue(svc.isBlacklisted(token));
    }
}

