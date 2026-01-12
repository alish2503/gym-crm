package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.jpa.TrainerJpaRepository;
import com.gymcrm.infrastructure.mapper.TraineeDaoMapper;
import com.gymcrm.infrastructure.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerDaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainerRepositoryImpl extends BaseRepositoryImpl<Trainer, TrainerDao> implements TrainerRepository {
private final TrainerJpaRepository trainerJpaRepository;

    @Autowired
    public TrainerRepositoryImpl(TrainerJpaRepository trainerJpaRepository) {
        super(trainerJpaRepository);
        this.trainerJpaRepository = trainerJpaRepository;
    }

    @Override
    public Optional<Trainer> findTrainerWithTrainees(String username) {
       return trainerJpaRepository.findWithTrainees(username)
                .map(dao -> {
                    List<Trainee> trainees = dao.getTrainees().stream().map(TraineeDaoMapper::toDomain).toList();
                    Trainer trainer = mapToDomain(dao);
                    trainer.setTrainees(trainees);
                    return trainer;
                });
    }

    @Override
    public Optional<Trainer> findTrainer(String username) {
        return trainerJpaRepository.findByUserUsername(username).map(this::mapToDomain);
    }

    @Override
    public List<Trainer> findAvailableTrainersNotAssignedAndActive(String traineeUsername) {
       return trainerJpaRepository.findAvailableTrainersForTrainee(traineeUsername).stream().map(
               this::mapToDomain
       ).toList();
    }

    @Override
    public List<Trainer> findTrainersByUserNamesIn(List<String> usernames) {
       return trainerJpaRepository.findByUserUsernameIn(usernames).stream().map(this::mapToDomain).toList();
    }

    @Override
    protected TrainerDao mapToDao(Trainer trainer) {
        return TrainerDaoMapper.toDao(trainer);
    }
    protected Trainer mapToDomain(TrainerDao trainerDao) {
        return TrainerDaoMapper.toDomain(trainerDao);
    }
}
