package com.gymcrm.application.facade.impl;

import com.gymcrm.application.facade.GymFacade;
import com.gymcrm.domain.model.*;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.application.service.TrainerService;
import com.gymcrm.application.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
@Service
public class GymFacadeImpl implements GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacadeImpl(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }


    @Override
    public void changePasswordForTrainee(String username, String oldPassword, String newPassword) {
        traineeService.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public void activateTrainee(String username, String password) {
        traineeService.activate(username, password);
    }

    @Override
    public void deactivateTrainee(String username, String password) {
        traineeService.deactivate(username, password);
    }

    @Override
    public void changePasswordForTrainer(String username, String oldPassword, String newPassword) {
        trainerService.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public void activateTrainer(String username, String password) {
        trainerService.activate(username, password);
    }

    @Override
    public void deactivateTrainer(String username, String password) {
        trainerService.deactivate(username, password);
    }

    @Override
    public Trainee getTraineeByUsername(String username, String password) {
        return traineeService.getTraineeByUserName(username, password);
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    @Override
    public void deleteTrainee(String username, String password) {
        traineeService.deleteTrainee(username, password);
    }

    @Override
    public List<Trainer> updateTrainersForTrainee(String username, String password, List<String> usernames) {
        return traineeService.updateTrainersForTrainee(username, password, usernames);
    }

    @Override
    public List<Trainer> getAvailableTrainersForTrainee(String username, String password) {
        return traineeService.getAvailableTrainersForTrainee(username, password);
    }

    @Override
    public Trainer getTrainerByUsername(String username, String password) {
        return trainerService.getTrainerByUserName(username, password);
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    @Override
    public void createTraining(Training training) {
        trainingService.createTraining(training);
    }

    @Override
    public List<Training> getTrainingsForTrainee(String traineeUserName, String password, LocalDate from,
                                                 LocalDate to, FullName trainerName, TrainingTypeEnum typeEnum)
    {
        return trainingService.getTrainingsForTrainee(traineeUserName, password, from, to, trainerName, typeEnum);
    }

    @Override
    public List<Training> getTrainingsForTrainer(String trainerUserName, String password, LocalDate from,
                                                 LocalDate to, FullName traineeName)
    {
        return trainingService.getTrainingsForTrainer(trainerUserName, password, from, to, traineeName);
    }
}
