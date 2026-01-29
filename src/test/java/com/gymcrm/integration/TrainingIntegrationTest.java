package com.gymcrm.integration;

import com.gymcrm.application.event.ActionType;
import com.gymcrm.application.event.TrainerWorkloadEvent;
import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.testconfig.NoSecurityTest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@NoSecurityTest
class TrainingIntegrationTest extends BaseIntegrationTest {
    private static final String URL_TRAININGS = "/trainings";
    private static final String TRAINER_JOHN = "John.Doe";
    private static final String TRAINEE_SARAH = "Sarah.Lee";
    private static final String TRAINING_NAME = "Resistance";
    private static final TrainingTypeEnum TRAINING_TYPE = TrainingTypeEnum.RESISTANCE;
    private final RabbitTemplate rabbitTemplate;
    private final TrainingRepository trainingRepository;

    @Value("${queue-name}")
    private String queueName;

    @Autowired
    public TrainingIntegrationTest(TestRestTemplate testRestTemplate, RabbitTemplate rabbitTemplate,
                                   TrainingRepository trainingRepository) {
        super(testRestTemplate);
        this.rabbitTemplate = rabbitTemplate;
        this.trainingRepository = trainingRepository;
    }

    @Test
    void shouldAddTrainingAndSendEvent() {
        LocalDate date = LocalDate.of(2026, 7, 1);
        CreateTrainingDto requestDto = new CreateTrainingDto(TRAINER_JOHN, TRAINEE_SARAH,
                TRAINING_TYPE, TRAINING_NAME, date, 2);

        ResponseEntity<Void> response = postTraining(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrainingExists(date, date);
        TrainerWorkloadEvent message = receiveEvent();
        assertNotNull(message);
        assertEquals(ActionType.ADD, message.actionType());
    }

    private ResponseEntity<Void> postTraining(CreateTrainingDto requestDto) {
        String url = UriComponentsBuilder.fromUriString(URL_TRAININGS).toUriString();
        return testRestTemplate.postForEntity(url, requestDto, Void.class);
    }

    private void assertTrainingExists(LocalDate from, LocalDate to) {
        FullName trainerName = new FullName(TRAINER_JOHN.split("\\.")[0], TRAINER_JOHN.split("\\.")[1]);
        TrainingFilter filter = new TrainingFilter(from, to, trainerName, TRAINING_TYPE);
        assertNotNull(trainingRepository.findTrainingsForTrainee(TRAINEE_SARAH, filter));
    }

    private TrainerWorkloadEvent receiveEvent() {
        return (TrainerWorkloadEvent) rabbitTemplate.receiveAndConvert(queueName);
    }
}
