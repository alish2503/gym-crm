package com.gymcrm.component;

import com.gymcrm.presentation.dto.response.TrainingForTraineeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetTrainingsForTraineeTest extends BaseComponentTest {
    private static final String BASE_URL = "/trainings/trainees/";
    private static final String USERNAME = "Emma.Brown";

    @Autowired
    public GetTrainingsForTraineeTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate);
    }

    @Test
    void shouldReturnTrainingsFilteredByDateAndType() {
        ResponseEntity<List<TrainingForTraineeDto>> response = getTrainings("2026-01-11", "2026-01-11", "YOGA");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        TrainingForTraineeDto training = response.getBody().get(0);
        assertEquals("Yoga Session", training.getTrainingName());
        assertEquals("YOGA", training.getType());
        assertEquals(LocalDate.of(2026, 1, 11), training.getDate());
    }

    @Test
    void shouldReturnTrainingsFilteredOnlyByDate() {
        ResponseEntity<List<TrainingForTraineeDto>> response = getTrainings("2026-01-10", "2026-01-11", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTrainingsFound() {
        ResponseEntity<List<TrainingForTraineeDto>> response = getTrainings("2026-02-01", "2026-02-10", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    private ResponseEntity<List<TrainingForTraineeDto>> getTrainings(String from, String to, String type) {
        String url = buildUrl(from, to, type);
        return testRestTemplate.exchange(url, HttpMethod.GET, buildEntity(), new ParameterizedTypeReference<>() {});
    }

    private HttpEntity<Void> buildEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private String buildUrl(String from, String to, String type) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL + USERNAME)
                .queryParam("from", from).queryParam("to", to);

        if (type != null) {
            builder.queryParam("type", type);
        }
        return builder.toUriString();
    }
}
