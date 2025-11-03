package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
class TrainerRepositoryImpl extends UserRepositoryImpl<Trainer, TrainerDao> implements TrainerRepository {

    @Autowired
    public TrainerRepositoryImpl(EntityManager entityManager) {
        super(entityManager, TrainerDao.class);
    }

    public Optional<Trainer> findTrainerWithTrainees(String userName) {
        String jpql = "select distinct t from TrainerDao t join fetch t.trainees where t.user.userName = :uName";

        return findDao(userName, jpql)
                .map(dao -> {
                    List<Trainee> trainees = dao.getTrainees().stream()
                            .map(TraineeMapper::toDomain)
                            .toList();
                    Trainer trainer = mapToDomain(dao);
                    trainer.setTrainees(trainees);
                    return trainer;
                });
    }

    @Override
    public List<Trainer> getAvailableTrainersNotAssigned(Trainee trainee) {
        List<Long> assigned = TraineeMapper.toDao(trainee).getTrainers().stream().map(TrainerDao::getId).toList();
        String jpql = "select t from TrainerDao t join fetch t.specialization where t.id not in :assigned";
        return entityManager.createQuery(jpql, TrainerDao.class)
                .setParameter("assigned", assigned)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Trainer> findTrainersByUserNamesIn(List<String> userNames) {
        String jpql = "select t from TrainerDao t join fetch t.specialization where t.user.userName in :unames";
        return entityManager.createQuery(jpql, TrainerDao.class).setParameter("unames", userNames).
                getResultList().stream().map(this::mapToDomain).toList();
    }

    @Override
    protected TrainerDao mapToDao(Trainer entity) {
        return TrainerMapper.toDao(entity);
    }

    @Override
    protected Trainer mapToDomain(TrainerDao dao) {
        return TrainerMapper.toDomain(dao);
    }
}
