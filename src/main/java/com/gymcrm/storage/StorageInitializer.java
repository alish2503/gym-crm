package com.gymcrm.storage;

import com.gymcrm.model.*;
import com.gymcrm.service.TraineeService;
import com.gymcrm.service.TrainerService;
import com.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
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
public class StorageInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private Resource initFile;

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public StorageInitializer(TraineeService traineeService,
                              TrainerService trainerService,
                              TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Initializing storage from file: {}", initFile.getFilename());
        loadData();
        log.info("Storage successfully initialized");
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(initFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                switch (parts[0].toUpperCase()) {
                    case "TRAINEE" -> {
                        traineeService.create(new Trainee(parts[1], parts[2],
                                LocalDate.parse(parts[3]), parts[4]));
                    }
                    case "TRAINER" -> {
                        TrainingType trainingType =
                                new TrainingType(TrainingTypeEnum.valueOf(parts[3].toUpperCase()));
                        trainerService.create(new Trainer(parts[1], parts[2], trainingType));
                    }
                    case "TRAINING" -> {
                        Trainee trainee = traineeService.getByUsername(parts[1]);
                        Trainer trainer = trainerService.getByUsername(parts[2]);
                        Training training = new Training(parts[3],
                                new TrainingType(TrainingTypeEnum.valueOf(parts[4].toUpperCase())),
                                LocalDate.parse(parts[5]),
                                Integer.parseInt(parts[6]),
                                trainer, trainee);
                        trainingService.create(training);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize storage: {}", e.getMessage(), e);
        }
    }
}

