package com.gymcrm.storage;

import com.gymcrm.model.*;
import com.gymcrm.service.TraineeService;
import com.gymcrm.service.TrainerService;
import com.gymcrm.service.TrainingService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
public class StorageInitializer implements BeanPostProcessor {

    @Value("${storage.init.file}")
    private Resource initFile;

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;

    @Autowired
    public void setTraineeService(TraineeService traineeService) { this.traineeService = traineeService; }

    @Autowired
    public void setTrainerService(TrainerService trainerService) { this.trainerService = trainerService; }

    @Autowired
    public void setTrainingService(TrainingService trainingService) { this.trainingService = trainingService; }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof InMemoryStorage storage) {
            loadData(storage);
        }
        return bean;
    }

    private void loadData(InMemoryStorage storage) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(initFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                switch (parts[0].toUpperCase()) {
                    case "TRAINEE" -> {
                        Trainee trainee = new Trainee();
                        trainee.setFirstName(parts[1]);
                        trainee.setLastName(parts[2]);
                        trainee.setDateOfBirth(LocalDate.parse(parts[3]));
                        trainee.setAddress(parts[4]);
                        traineeService.create(trainee);
                    }
                    case "TRAINER" -> {
                        Trainer trainer = new Trainer();
                        TrainingType trainingType = new TrainingType(TrainingTypeEnum.valueOf(parts[3]));
                        trainer.setFirstName(parts[1]);
                        trainer.setLastName(parts[2]);
                        trainer.setSpecialization(trainingType);
                        trainerService.create(trainer);
                    }
                    case "TRAINING" -> {
                        String traineeUsername = parts[1];
                        String trainerUsername = parts[2];
                        String name = parts[3];
                        TrainingType trainingType = new TrainingType(TrainingTypeEnum.valueOf(parts[4]));
                        LocalDate date = LocalDate.parse(parts[5]);
                        int duration = Integer.parseInt(parts[6]);
                        Trainee trainee = traineeService.getByUsername(traineeUsername);
                        Trainer trainer = trainerService.getByUsername(trainerUsername);
                        if (trainee == null || trainer == null) {
                            System.err.println("Skipping training: trainer or trainee not found");
                            continue;
                        }
                        Training training = new Training(name, trainingType, date, duration, trainer, trainee);
                        trainingService.create(training);
                    }
                }
            }
            System.out.println("Storage initialized from " + initFile.getFilename());
        } catch (Exception e) {
            System.err.println("Failed to initialize storage: " + e.getMessage());
        }
    }
}
