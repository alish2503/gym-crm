package com.gymcrm.component;

import com.gymcrm.infrastructure.security.service.JwtService;
import com.gymcrm.presentation.dto.request.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("security-test")
class AuthComponentTest extends BaseComponentTest {
    private final JwtService jwtService;
    private static final String BASE_URL = "/auth";
    private static final String USERNAME1 = "Emma.Brown";
    private static final String USERNAME2 = "Mike.Wilson";
    private static final String CORRECT_PASSWORD = "pass";
    private static final String WRONG_PASSWORD = "wrong";

    @Value("${security.login.max-attempts}")
    private int MAX_LOGIN_ATTEMPTS;

    @Autowired
    public AuthComponentTest(TestRestTemplate testRestTemplate, JwtService jwtService)
    {
        super(testRestTemplate);
        this.jwtService = jwtService;
    }

    @Test
    void loginWithValidCredentials_shouldReturnToken() {
        LoginDto loginDto = new LoginDto(USERNAME1, CORRECT_PASSWORD);
        ResponseEntity<Map> response = testRestTemplate.postForEntity(BASE_URL + "/login", loginDto, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("accessToken"));
        assertNotNull(body.get("accessToken"));
    }

    @Test
    void loginWithWrongPassword_shouldReturn401() {
        LoginDto loginDto = new LoginDto(USERNAME1, WRONG_PASSWORD);
        ResponseEntity<Map> response = testRestTemplate.postForEntity(BASE_URL + "/login", loginDto, Map.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
    }

    @Test
    void loginAfterMultipleWrongAttempts_shouldReturn429() {
        LoginDto loginDto = new LoginDto(USERNAME2, WRONG_PASSWORD);
        for (int i = 0; i < MAX_LOGIN_ATTEMPTS; ++i) {
            testRestTemplate.postForEntity(BASE_URL + "/login", loginDto, Map.class);
        }
        ResponseEntity<Map> blockedResponse = testRestTemplate.postForEntity(BASE_URL + "/login", loginDto, Map.class);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blockedResponse.getStatusCode());
        Map<String, String> body = blockedResponse.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
        assertTrue(body.get("error").toLowerCase().contains("blocked"));
    }

    @Test
    void logoutWithValidToken_shouldReturn200() {
        String token = jwtService.generateTokenForUser(USERNAME1);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.postForEntity(BASE_URL + "/logout", entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void logoutWithoutToken_shouldReturn401() {
        ResponseEntity<Map> response = testRestTemplate.postForEntity(BASE_URL + "/logout", null, Map.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
    }

    @Test
    void logoutWithInvalidToken_shouldReturn401() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid.jwt.token");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = testRestTemplate.postForEntity(BASE_URL + "/logout", entity, Map.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
    }
}
