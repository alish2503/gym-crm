package com.gymcrm.component;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.security.service.CustomUserDetailsService;
import com.gymcrm.presentation.dto.request.ChangePasswordDto;
import com.gymcrm.testconfig.NoSecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoSecurityTest
class UserComponentTest extends BaseComponentTest {
    private final UserProfileRepository userProfileRepository;
    private final CustomUserDetailsService userDetailsService;
    private static final String BASE_URL = "/users";
    private static final String USER_JOHN = "John.Doe";

    @Autowired
    public UserComponentTest(TestRestTemplate testRestTemplate, UserProfileRepository userProfileRepository,
                             CustomUserDetailsService userDetailsService) {
        super(testRestTemplate);
        this.userProfileRepository = userProfileRepository;
        this.userDetailsService = userDetailsService;
    }

    @Test
    void shouldToggleUserActivity() {
        User before = getUser();
        boolean initialState = before.isActive();

        ResponseEntity<Void> response = patch(buildActiveUrl(), null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User after = getUser();
        assertNotEquals(initialState, after.isActive());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        ChangePasswordDto dto = new ChangePasswordDto("pass", "newPassword");

        ResponseEntity<Void> response = patch(buildPasswordUrl(), dto, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(userDetailsService.isValidPassword(USER_JOHN, "newPassword"));
    }

    @Test
    void shouldReturnUnauthorizedWhenOldPasswordIsWrong() {
        ChangePasswordDto dto = new ChangePasswordDto("wrongPassword", "newPassword");

        ResponseEntity<String> response = patch(buildPasswordUrl(), dto, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(userDetailsService.isValidPassword(USER_JOHN, "pass"));
    }

    private User getUser() {
        return userProfileRepository.findProfileByUsername(USER_JOHN).get();
    }

    private String buildActiveUrl() {
        return UriComponentsBuilder.fromUriString(BASE_URL + "/" + USER_JOHN + "/active").toUriString();
    }

    private String buildPasswordUrl() {
        return UriComponentsBuilder.fromUriString(BASE_URL + "/" + USER_JOHN + "/password").toUriString();
    }

    private <T> ResponseEntity<T> patch(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return testRestTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
    }
}
