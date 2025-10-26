package service;

import com.gymcrm.application.service.PasswordService;
import com.gymcrm.infrastructure.service.PasswordServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
public class PasswordGeneratorTest {
    private final PasswordService passwordService = new PasswordServiceImpl();

    @Test
    void shouldGeneratePasswordWithCorrectLength() {
        int length = 10;
        String password = passwordService.generateRandomPassword(length);
        assertEquals(length, password.length());
    }

    @Test
    void shouldContainOnlyAllowedCharacters() {
        int length = 20;
        String password = passwordService.generateRandomPassword(length);
        String allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (char c : password.toCharArray()) {
            assertTrue(allowed.indexOf(c) >= 0);
        }
    }
}
