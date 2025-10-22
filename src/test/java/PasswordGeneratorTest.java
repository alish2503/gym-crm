import com.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alish
 */
public class PasswordGeneratorTest {
    @Test
    void testGenerateRandomPassword_LengthAndChars() {
        String pwd = PasswordGenerator.generateRandomPassword(10);
        assertEquals(10, pwd.length());
        assertTrue(pwd.chars().allMatch(ch ->
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".indexOf(ch) >= 0
        ));
    }

    @Test
    void testGenerateRandomPassword_DifferentPasswords() {
        String pwd1 = PasswordGenerator.generateRandomPassword(10);
        String pwd2 = PasswordGenerator.generateRandomPassword(10);
        assertNotEquals(pwd1, pwd2);
    }
}
