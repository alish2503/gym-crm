package application;

import com.gymcrm.application.service.impl.UserServiceImpl;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John.Doe", "hashed", "John", "Doe", true);
    }

    @Test
    void changePassword_shouldEncodeAndUpdateProfile() {
        when(encoder.encode("newPass")).thenReturn("newHash");
        when(userProfileRepository.findProfileByUsername("John.Doe")).thenReturn(Optional.of(user));
        userService.changePassword("John.Doe", "newPass");
        verify(userProfileRepository).saveOrUpdate(user);
        assertEquals("newHash", user.getPassword());
    }

    @Test
    void toggle_shouldChangeActiveValue() {
        when(userProfileRepository.findProfileByUsername("John.Doe")).thenReturn(Optional.of(user));
        userService.toggle("John.Doe");
        verify(userProfileRepository).saveOrUpdate(user);
        assertFalse(user.isActive());
    }
}
