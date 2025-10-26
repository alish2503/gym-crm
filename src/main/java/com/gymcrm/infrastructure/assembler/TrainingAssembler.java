package com.gymcrm.infrastructure.assembler;

import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.mapper.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */
@Component
public class TrainingAssembler {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainingAssembler(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    public Training mapToDomain(TrainingDao dao) {
        Trainee trainee = traineeRepository.findByUsername(dao.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + dao.getTraineeUsername()));

        Trainer trainer = trainerRepository.findByUsername(dao.getTrainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + dao.getTrainerUsername()));

        return TrainingMapper.toDomain(dao, trainer, trainee);
    }
}
