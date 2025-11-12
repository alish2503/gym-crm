package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TraineeRepositoryImpl extends UserRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {

    TraineeRepositoryImpl() {
        super(TraineeDao.class);
    }

    @Override
    public void deleteById(Long id) {
        TraineeDao dao = entityManager.find(TraineeDao.class, id);
        entityManager.remove(dao);
    }

    @Override
    public Optional<Trainee> findTraineeWithTrainers(String username) {
        String jpql = "select distinct t from TraineeDao t left join fetch t.trainers tr " +
                "left join fetch tr.user where t.user.username = :uName";

        return entityManager.createQuery(jpql, TraineeDao.class).
                setParameter("uName", username)
                .getResultStream()
                .findFirst()
                .map(dao -> {
                    List<Trainer> trainers = dao.getTrainers().stream()
                            .map(TrainerMapper::toDomainWithProfile)
                            .toList();
                    Trainee trainee = TraineeMapper.toDomain(dao);
                    trainee.setTrainers(trainers);
                    return trainee;
                });
    }

    @Override
    protected TraineeDao mapToDao(Trainee entity) {
        return TraineeMapper.toDao(entity);
    }
}
