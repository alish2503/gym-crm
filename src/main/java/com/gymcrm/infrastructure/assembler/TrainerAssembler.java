package com.gymcrm.infrastructure.assembler;

import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alish
 */
@Component
public class TrainerAssembler {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainerAssembler(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public Trainer mapToDomain(TrainerDao dao) {
        TrainingType type = trainingTypeRepository.findByName(dao.getSpecialization())
                .orElseThrow(() -> new EntityNotFoundException("Training type not found: " + dao.getSpecialization()));

        return TrainerMapper.toDomain(dao, type);
    }
}
