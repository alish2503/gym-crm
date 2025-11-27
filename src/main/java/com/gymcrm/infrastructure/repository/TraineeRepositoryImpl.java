package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.mapper.TrainerDaoMapper;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeDaoMapper;
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
        String jpql = "select distinct t from TraineeDao t left join fetch t.trainers where t.user.username = :uName";

        return entityManager.createQuery(jpql, TraineeDao.class).
                setParameter("uName", username)
                .getResultStream()
                .findFirst()
                .map(dao -> {
                    List<Trainer> trainers = dao.getTrainers().stream()
                            .map(TrainerDaoMapper::toDomain)
                            .toList();
                    Trainee trainee = TraineeDaoMapper.toDomain(dao);
                    trainee.setTrainers(trainers);
                    return trainee;
                });
    }

    @Override
    protected TraineeDao mapToDao(Trainee entity) {
        return TraineeDaoMapper.toDao(entity);
    }
}
