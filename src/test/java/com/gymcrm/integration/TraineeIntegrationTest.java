package com.gymcrm.integration;

import com.gymcrm.application.event.ActionType;
import com.gymcrm.application.event.TrainerWorkloadEvent;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.testconfig.NoSecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoSecurityTest
class TraineeIntegrationTest extends BaseIntegrationTest {
    private static final String TRAINEE_EMMA = "Emma.Brown";
    private static final int TRAININGS_COUNT = 3;
    private final RabbitTemplate rabbitTemplate;
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    public TraineeIntegrationTest(TestRestTemplate testRestTemplate, RabbitTemplate rabbitTemplate,
                                  TrainingRepository trainingRepository, TraineeRepository traineeRepository) {
        super(testRestTemplate);
        this.rabbitTemplate = rabbitTemplate;
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
    }

    @Test
    void shouldDeleteTraineeAndAllTrainingsAndSendEvents() {
        List<Training> trainingsBefore = getTrainingsForTrainee();
        assertEquals(TRAININGS_COUNT, trainingsBefore.size());
        ResponseEntity<Void> response = deleteTrainee();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(traineeRepository.findTrainee(TRAINEE_EMMA).isEmpty());
        List<Training> trainingsAfter = getTrainingsForTrainee();
        assertTrue(trainingsAfter.isEmpty());
        assertRabbitMessages();
    }

    private List<Training> getTrainingsForTrainee() {
        TrainingFilter filter = new TrainingFilter(null, null, null, null);
        return trainingRepository.findTrainingsForTrainee(TRAINEE_EMMA, filter);
    }

    private ResponseEntity<Void> deleteTrainee() {
        String url = UriComponentsBuilder.fromUriString("/trainees/" + TRAINEE_EMMA).toUriString();
        return testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }

    private void assertRabbitMessages() {
        for (int i = 0; i < TRAININGS_COUNT; ++i) {
            TrainerWorkloadEvent message = (TrainerWorkloadEvent) rabbitTemplate.receiveAndConvert(queueName);
            assertNotNull(message);
            assertEquals(ActionType.DELETE, message.actionType());
        }
    }
}
