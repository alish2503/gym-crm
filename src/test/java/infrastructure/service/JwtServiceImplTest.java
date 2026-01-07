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

    private static final String SECRET = "0123456789abcdef0123456789abcdef";
    private static final String USERNAME = "John.Doe";

    private static final long USER_TOKEN_EXP_MS = 60_000;
    private static final long SERVICE_TOKEN_EXP_MS = 120_000;

    private JwtService jwt;

    @BeforeEach
    void setUp() {
        jwt = new JwtServiceImpl(SECRET, USER_TOKEN_EXP_MS, SERVICE_TOKEN_EXP_MS);
    }

    @Test
    void generateTokenForUser_ok() {
        String token = jwt.generateTokenForUser(USERNAME);
        assertTrue(jwt.isValidToken(token));
        assertEquals(USERNAME, jwt.getUsername(token));
    }

    @Test
    void generateTokenForService_ok() {
        String token = jwt.generateTokenForService();
        assertTrue(jwt.isValidToken(token));
    }

    @Test
    void isValidToken_invalid() {
        String invalid = "broken.token";
        assertFalse(jwt.isValidToken(invalid));
    }

    @Test
    void getExpiration_userToken() {
        long before = System.currentTimeMillis();
        String token = jwt.generateTokenForUser(USERNAME);
        long after = System.currentTimeMillis();
        Date exp = jwt.getExpiration(token);
        assertTrue(exp.getTime() > before);
        assertTrue(exp.getTime() <= after + USER_TOKEN_EXP_MS);
    }

    @Test
    void getExpiration_serviceToken() {
        long before = System.currentTimeMillis();
        String token = jwt.generateTokenForService();
        long after = System.currentTimeMillis();
        Date exp = jwt.getExpiration(token);
        assertTrue(exp.getTime() > before);
        assertTrue(exp.getTime() <= after + SERVICE_TOKEN_EXP_MS);
    }

    @Test
    void isValidToken_expired_userToken() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, 50, SERVICE_TOKEN_EXP_MS);
        String token = shortJwt.generateTokenForUser(USERNAME);
        Thread.sleep(70);
        assertFalse(shortJwt.isValidToken(token));
    }

    @Test
    void getUsername_expired_userToken() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, 50, SERVICE_TOKEN_EXP_MS);
        String token = shortJwt.generateTokenForUser(USERNAME);
        Thread.sleep(70);
        assertThrows(ExpiredJwtException.class, () -> shortJwt.getUsername(token));
    }

    @Test
    void isValidToken_expired_serviceToken() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, USER_TOKEN_EXP_MS, 50);
        String token = shortJwt.generateTokenForService();
        Thread.sleep(70);
        assertFalse(shortJwt.isValidToken(token));
    }

    @Test
    void getUsername_expired_serviceToken() throws InterruptedException {
        JwtService shortJwt = new JwtServiceImpl(SECRET, USER_TOKEN_EXP_MS, 50);
        String token = shortJwt.generateTokenForService();
        Thread.sleep(70);
        assertThrows(ExpiredJwtException.class, () -> shortJwt.getUsername(token));
    }
}


