package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.infrastructure.dao.TrainingDao;
import com.gymcrm.infrastructure.mapper.TrainingDaoMapper;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@Repository
public class TrainingRepositoryImpl extends BaseRepositoryImpl<Training, TrainingDao> implements TrainingRepository {

    @Override
    public List<Training> findTrainingsForTrainee(String username, TrainingFilter trainingFilter) {
        LocalDate from = trainingFilter.from();
        LocalDate to = trainingFilter.to();
        FullName trainerName = trainingFilter.personName();
        TrainingTypeEnum typeEnum = trainingFilter.type();
        String jpql = "select tr from TrainingDao tr join fetch tr.trainer where tr.trainee.user.username = :uname";
        Map<String, Object> params = new HashMap<>();
        params.put("uname", username);
        jpql = appendDateAndNameFilters(jpql, params, from, to, trainerName, "trainer");
        if (typeEnum != null) {
            jpql += " and tr.type.name = :tType ";
            params.put("tType", typeEnum);
        }
        TypedQuery<TrainingDao> query = entityManager.createQuery(jpql, TrainingDao.class);
        params.forEach(query::setParameter);
        return query.getResultList().stream().map(TrainingDaoMapper::toDomainForTrainee).toList();
    }

    @Override
    public List<Training> findTrainingsForTrainer(String userName, TrainingFilter trainingFilter) {
        LocalDate from = trainingFilter.from();
        LocalDate to = trainingFilter.to();
        FullName traineeName = trainingFilter.personName();
        String jpql = "select tr from TrainingDao tr join fetch tr.trainee where tr.trainer.user.username = :uname ";
        Map<String, Object> params = new HashMap<>();
        params.put("uname", userName);
        jpql = appendDateAndNameFilters(jpql, params, from, to, traineeName, "trainee");
        TypedQuery<TrainingDao> query = entityManager.createQuery(jpql, TrainingDao.class);
        params.forEach(query::setParameter);
        return query.getResultList().stream().map(TrainingDaoMapper::toDomainForTrainer).toList();
    }

    @Override
    public boolean existsTraining(String trainerUsername, String traineeUsername,
                                  LocalDate trainingDate, String trainingName) {

        String jpql = "select count(t) from TrainingDao t where t.trainer.user.username = :trainerUsername " +
                "and t.trainee.user.username = :traineeUsername and t.date = :tDate " +
                "and t.name = :tName";

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("trainerUsername", trainerUsername)
                .setParameter("traineeUsername", traineeUsername)
                .setParameter("tDate", trainingDate)
                .setParameter("tName", trainingName)
                .getSingleResult();

        return count > 0;
    }



    private String appendDateAndNameFilters(String jpql, Map<String, Object> params, LocalDate from,
                                            LocalDate to, FullName name, String aliasPrefix)
    {

        if (from != null) {
            jpql += " and tr.date >= :from ";
            params.put("from", from);
        }
        if (to != null) {
            jpql += " and tr.date <= :to ";
            params.put("to", to);
        }
        if (name != null) {
            jpql += " and " + aliasPrefix + ".user.firstName = :fName " +
                    " and " + aliasPrefix + ".user.lastName = :lName ";
            params.put("fName", name.getFirstName());
            params.put("lName", name.getLastName());
        }
        return jpql;
    }

    @Override
    protected TrainingDao mapToDao(Training entity) {
        return TrainingDaoMapper.toDao(entity);
    }
}

