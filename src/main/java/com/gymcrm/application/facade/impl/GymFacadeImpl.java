package com.gymcrm.application.facade.impl;

import com.gymcrm.application.facade.GymFacade;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.application.service.TrainerService;
import com.gymcrm.application.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Trainee createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        traineeService.update(trainee);
    }

    @Override
    public void deleteTrainee(String username) {
        traineeService.delete(username);
    }

    @Override
    public Trainee getTraineeByUserName(String userName) {
        return traineeService.getByUsername(userName);
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        trainerService.update(trainer);
    }

    @Override
    public Trainer getTrainerByUserName(String userName) {
        return trainerService.getByUsername(userName);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingService.getById(id);
    }
}
