package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainerRepositoryImpl extends UserRepositoryImpl<Trainer, TrainerDao> implements TrainerRepository {

    public TrainerRepositoryImpl() {
        super(TrainerDao.class);
    }

    public Optional<Trainer> findTrainerWithTrainees(String username) {
        String jpql = "select distinct t from TrainerDao t join fetch t.user user " +
                "left join fetch t.trainees tr left join fetch tr.user where user = :uName";

        return entityManager.createQuery(jpql, TrainerDao.class).
                setParameter("uName", username)
                .getResultStream()
                .findFirst()
                .map(dao -> {
                    List<Trainee> trainees = dao.getTrainees().stream()
                            .map(TraineeMapper::toDomainWithProfile)
                            .toList();
                    Trainer trainer = TrainerMapper.toDomain(dao);
                    trainer.setTrainees(trainees);
                    return trainer;
                });
    }

    @Override
    public List<Trainer> getAvailableTrainersNotAssigned(List<Long> assignedIds) {
        String jpql = "select t from TrainerDao t join fetch t.user user " +
                    "where t.id not in :assigned and user.isActive = true";

        return entityManager.createQuery(jpql, TrainerDao.class)
                .setParameter("assigned", assignedIds)
                .getResultList()
                .stream()
                .map(TrainerMapper::toDomainWithProfile)
                .toList();
    }

    @Override
    public List<Trainer> findTrainersByUserNamesIn(List<String> usernames) {
        String jpql = "select t from TrainerDao t join fetch t.user u " +
                    "where u.username in :uNames";

        return entityManager.createQuery(jpql, TrainerDao.class).setParameter("uNames", usernames).
                getResultList().stream().map(TrainerMapper::toDomainWithProfile).toList();
    }

    @Override
    public List<Long> findAssignedTrainersIds(String traineeUsername) {
        String jpql = "select tr.id  from TraineeDao t join t.trainers tr where t.user.username = :uName";
        return entityManager.createQuery(jpql, Long.class).setParameter("uName", traineeUsername).getResultList();
    }

    @Override
    public List<Trainer> findAll() {
        String jpql = "select t from TrainerDao t join fetch t.user";
        return entityManager.createQuery(jpql, TrainerDao.class).getResultList().stream().
                map(TrainerMapper::toDomainWithProfile).toList();
    }

    @Override
    protected TrainerDao mapToDao(Trainer entity) {
        return TrainerMapper.toDao(entity);
    }
}
