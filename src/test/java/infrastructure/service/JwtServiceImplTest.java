package infrastructure.service;

import com.gymcrm.infrastructure.security.service.impl.JwtServiceImpl;
import com.gymcrm.infrastructure.security.service.port.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alish
 */

class JwtServiceImplTest {
    private static final String SECRET = "0123456789abcdef0123456789abcdef"; // 32 bytes
    private JwtService jwt;

    @BeforeEach
    void setUp() {
        jwt = new JwtServiceImpl(SECRET, 1000L * 60);
    }

    @Test
    void generateToken_ok() {
        String username = "John.Doe";
        String token = jwt.generateToken(username);
        assertTrue(jwt.isValidToken(token));
        assertEquals(username, jwt.getUsername(token));
    }

    @Test
    void isValidToken_invalid() {
        String invalid = "broken.token";
        boolean valid = jwt.isValidToken(invalid);
        assertFalse(valid);
    }

    @Test
    void getExpiration_ok() {
        long before = System.currentTimeMillis();
        String token = jwt.generateToken("user");
        long after = System.currentTimeMillis();
        Date exp = jwt.getExpiration(token);
        assertTrue(exp.getTime() > before);
        assertTrue(exp.getTime() < after + 60_000);
    }

    @Test
    void isValidToken_expired() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, 50);
        String token = shortJwt.generateToken("user");
        Thread.sleep(70);
        boolean valid = shortJwt.isValidToken(token);
        assertFalse(valid);
    }

    @Test
    void getUsername_expired() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, 50);
        String token = shortJwt.generateToken("user");
        Thread.sleep(70);
        assertThrows(ExpiredJwtException.class, () -> shortJwt.getUsername(token));
    }
}

