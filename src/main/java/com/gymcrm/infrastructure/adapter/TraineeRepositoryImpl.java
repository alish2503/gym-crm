package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.jpa.TraineeJpaRepository;
import com.gymcrm.infrastructure.mapper.TrainerDaoMapper;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeDaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TraineeRepositoryImpl extends BaseRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {
    private final TraineeJpaRepository traineeJpaRepository;

    @Autowired
    TraineeRepositoryImpl(TraineeJpaRepository traineeJpaRepository) {
        super(traineeJpaRepository);
        this.traineeJpaRepository = traineeJpaRepository;
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        traineeJpaRepository.delete(mapToDao(trainee));
    }

    @Override
    public Optional<Trainee> findTraineeWithTrainers(String username) {
        return traineeJpaRepository.findWithTrainers(username)
                .map(dao -> {
                    List<Trainer> trainers = dao.getTrainers().stream()
                            .map(TrainerDaoMapper::toDomain)
                            .toList();
                    Trainee trainee = mapToDomain(dao);
                    trainee.setTrainers(trainers);
                    return trainee;
                });
    }

    @Override
    public Optional<Trainee> findTrainee(String username) {
        return traineeJpaRepository.findByUserUsername(username).map(this::mapToDomain);
    }

    @Override
    public Optional<Long> findTraineeId(String username) {
        return traineeJpaRepository.findIdByUsername(username);
    }

    @Override
    protected TraineeDao mapToDao(Trainee trainee) {
        return TraineeDaoMapper.toDao(trainee);
    }
    protected Trainee mapToDomain(TraineeDao traineeDao) {
        return TraineeDaoMapper.toDomain(traineeDao);
    }
}
