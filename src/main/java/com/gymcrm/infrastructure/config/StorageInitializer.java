package com.gymcrm.infrastructure.config;

import com.gymcrm.application.facade.GymFacade;
import com.gymcrm.domain.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * @author Alish
 */
@Component
public class StorageInitializer {

    private final GymFacade gymFacade;
    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private Resource initFile;

    @Autowired
    public StorageInitializer(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostConstruct
    public void init() {
        if (initFile == null || !initFile.exists()) {
            log.info("Init file not found, skipping storage initialization");
            return;
        }
        log.info("Initializing storage from file: {}", initFile.getFilename());
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(initFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                switch (parts[0].toUpperCase()) {
                    case "TRAINEE" -> gymFacade.createTrainee(
                            new Trainee(parts[1], parts[2], true, LocalDate.parse(parts[3]), parts[4])
                    );

                    case "TRAINER" -> gymFacade.createTrainer(
                            new Trainer(parts[1], parts[2], true,
                                    new TrainingType(TrainingTypeEnum.valueOf(parts[3].toUpperCase())))
                    );

                    case "TRAINING" -> {
                        Trainee trainee = gymFacade.getTraineeByUserName(parts[1]);
                        Trainer trainer = gymFacade.getTrainerByUserName(parts[2]);
                        Training training = new Training(
                                parts[3],
                                new TrainingType(TrainingTypeEnum.valueOf(parts[4].toUpperCase())),
                                LocalDate.parse(parts[5]),
                                Integer.parseInt(parts[6]),
                                trainer,
                                trainee
                        );
                        gymFacade.createTraining(training);
                    }
                }
            }
            log.info("Storage successfully initialized");
        } catch (Exception e) {
            log.error("Failed to initialize storage: {}", e.getMessage(), e);
        }
    }
}

