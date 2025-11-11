package com.gymcrm.application.facade.impl;

import com.gymcrm.application.facade.GymFacade;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.application.service.TrainerService;
import com.gymcrm.application.service.TrainingService;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alish
 */
@Service
class GymFacadeImpl implements GymFacade {

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
    public void changePasswordForTrainee(UserCredentials credentials, String newPassword) {
        traineeService.changePassword(credentials, newPassword);
    }

    @Override
    public void toggleTrainee(UserCredentials credentials) {
        traineeService.toggle(credentials);
    }

    @Override
    public void changePasswordForTrainer(UserCredentials credentials, String newPassword) {
        trainerService.changePassword(credentials, newPassword);
    }

    @Override
    public void toggleTrainer(UserCredentials credentials) {
        trainerService.toggle(credentials);
    }

    @Override
    public Trainee getTraineeByUsername(UserCredentials credentials) {
        return traineeService.getTraineeByUserName(credentials);
    }

    @Override
    public UserCredentials createTrainee(CreateTraineeRequest request) {
        return traineeService.createTrainee(request);
    }

    @Override
    public Trainee updateTrainee(UpdateTraineeRequest request, UserCredentials credentials) {
        return traineeService.updateTrainee(request, credentials);
    }

    @Override
    public void deleteTrainee(UserCredentials credentials) {
        traineeService.deleteTrainee(credentials);
    }

    @Override
    public List<Trainer> updateTrainersForTrainee(UserCredentials credentials, List<String> usernames) {
        return traineeService.updateTrainersForTrainee(credentials, usernames);
    }

    @Override
    public List<Trainer> getAvailableTrainersForTrainee(UserCredentials credentials) {
        return traineeService.getAvailableTrainersForTrainee(credentials);
    }

    @Override
    public Trainer getTrainerByUsername(UserCredentials credentials) {
        return trainerService.getTrainerByUserName(credentials);
    }

    @Override
    public UserCredentials createTrainer(CreateTrainerRequest request) {
        return trainerService.createTrainer(request);
    }

    @Override
    public Trainer updateTrainer(UpdateTrainerRequest request, UserCredentials credentials) {
        return trainerService.updateTrainer(request, credentials);
    }

    @Override
    public void createTraining(CreateTrainingRequest request) {
        trainingService.createTraining(request);
    }

    @Override
    public List<Training> getTrainingsForTrainee(UserCredentials credentials, TrainingFilter trainingFilter)
    {
        return trainingService.getTrainingsForTrainee(credentials, trainingFilter);
    }

    @Override
    public List<Training> getTrainingsForTrainer(UserCredentials credentials, TrainingFilter trainingFilter)
    {
        return trainingService.getTrainingsForTrainer(credentials, trainingFilter);
    }
}
