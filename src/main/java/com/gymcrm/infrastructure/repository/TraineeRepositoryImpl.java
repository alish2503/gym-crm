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
class TraineeRepositoryImpl extends UserRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {

    TraineeRepositoryImpl() {
        super(TraineeDao.class);
    }

    @Override
    public void delete(String username) {
        entityManager.createQuery("delete from trainees t where t.user.userName = :u")
                .setParameter("u", username)
                .executeUpdate();
    }

    @Override
    public Optional<Trainee> findTraineeWithTrainers(String userName) {
        String jpql = "select distinct t from TraineeDao t join fetch t.trainers tr join fetch tr.user " +
                    "join fetch tr.specialization where t.user.userName = :uName";

        return entityManager.createQuery(jpql, TraineeDao.class).
                setParameter("uName", userName)
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
