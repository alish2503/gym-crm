package com.gymcrm.facade.impl;

import com.gymcrm.facade.GymFacade;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import com.gymcrm.service.TraineeService;
import com.gymcrm.service.TrainerService;
import com.gymcrm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Trainee> getAllTrainees() {
        return traineeService.getAll();
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
    public List<Trainer> getAllTrainers() {
        return trainerService.getAll();
    }

    @Override
    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    @Override
    public Training getTrainingById(Long id) {
        return trainingService.getById(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingService.getAll();
    }
}
