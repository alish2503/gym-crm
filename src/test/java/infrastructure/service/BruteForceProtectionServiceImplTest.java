package infrastructure.service;

import com.gymcrm.infrastructure.security.service.impl.BruteForceProtectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */

class BruteForceProtectionServiceImplTest {
    private TestableBruteForceProtectionService service;
    private long fakeTime;

    private static class TestableBruteForceProtectionService extends BruteForceProtectionServiceImpl {
        private long fixedTime;

        public TestableBruteForceProtectionService(int maxAttempts, long blockMinutes) {
            super(maxAttempts, blockMinutes);
        }

        public void setFixedTime(long time) {
            this.fixedTime = time;
        }

        @Override
        protected long now() {
            return fixedTime;
        }
    }

    @BeforeEach
    void setUp() {
        service = new TestableBruteForceProtectionService(3, 1);
        fakeTime = 1000L;
        service.setFixedTime(fakeTime);
    }

    @Test
    void loginSucceeded_resetsAttempts() {
        service.loginFailed("user1");
        service.loginSucceeded("user1");
        assertEquals(0, service.checkBlocked("user1"));
    }

    @Test
    void loginFailed_triggersBlockAfterMaxAttempts() {
        service.loginFailed("user1");
        service.loginFailed("user1");
        service.loginFailed("user1");
        long remaining = service.checkBlocked("user1");
        assertEquals(60_000, remaining);
    }

    @Test
    void checkBlocked_unblocksAfterTimePassed() {
        service.loginFailed("user1");
        service.loginFailed("user1");
        service.loginFailed("user1");
        service.setFixedTime(fakeTime + 30_000);
        assertEquals(30_000, service.checkBlocked("user1"));
        service.setFixedTime(fakeTime + 62_000);
        assertEquals(0, service.checkBlocked("user1"));
    }
}

