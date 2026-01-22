package com.gymcrm.infrastructure.persistence.adapter;

import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.jpa.TrainingJpaRepository;
import com.gymcrm.infrastructure.persistence.mapper.TrainingDaoMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
@Repository
public class TrainingRepositoryImpl extends BaseRepositoryImpl<Training, TrainingDao> implements TrainingRepository {
    private final TrainingJpaRepository trainingJpaRepository;

    @Autowired
    public TrainingRepositoryImpl(TrainingJpaRepository trainingJpaRepository)
    {
        super(trainingJpaRepository);
        this.trainingJpaRepository = trainingJpaRepository;
    }

    @Override
    public List<Training> findTrainingsForTrainee(String username, TrainingFilter trainingFilter) {
        Specification<TrainingDao> specification = buildSpecificationForTrainee(username, trainingFilter);
        return trainingJpaRepository.findAll(specification).stream().map(
                TrainingDaoMapper::toDomainForTrainee
        ).toList();
    }

    @Override
    public List<Training> findTrainingsForTrainer(String username, TrainingFilter trainingFilter) {
        Specification<TrainingDao> specification = buildSpecificationForTrainer(username, trainingFilter);
        return trainingJpaRepository.findAll(specification).stream().map(
                TrainingDaoMapper::toDomainForTrainer
        ).toList();
    }

    @Override
    public boolean existsTraining(String trainerUsername, String traineeUsername,
                                  LocalDate trainingDate, String trainingName)
    {
        return trainingJpaRepository.existsByTrainerUserUsernameAndTraineeUserUsernameAndDateAndName(
                trainerUsername, traineeUsername, trainingDate, trainingName
        );
    }

    private Predicate applyFilters(Root<TrainingDao> root, CriteriaBuilder cb, String username,
                                   TrainingFilter filter, String userAlias, String personAlias)
    {
        Predicate predicate = cb.equal(root.get(userAlias).get("user").get("username"), username);
        if (filter.from() != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), filter.from()));
        }
        if (filter.to() != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), filter.to()));
        }
        if (filter.personName() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get(personAlias).get("user").get("firstName"), filter.personName().getFirstName()),
                    cb.equal(root.get(personAlias).get("user").get("lastName"), filter.personName().getLastName())
            );
        }
        if (filter.type() != null) {
            predicate = cb.and(predicate,
                    cb.equal(root.get("type").get("name"), filter.type())
            );
        }
        return predicate;
    }

    private Specification<TrainingDao> buildSpecificationForTrainee(String username, TrainingFilter filter) {
        return (root, query, cb) -> {
            root.fetch("trainer");
            query.distinct(true);
            return applyFilters(root, cb, username, filter, "trainee", "trainer");
        };
    }

    private Specification<TrainingDao> buildSpecificationForTrainer(String username, TrainingFilter filter) {
        return (root, query, cb) -> {
            root.fetch("trainee");
            query.distinct(true);
            return applyFilters(root, cb, username, filter, "trainer", "trainee");
        };
    }

    @Override
    protected TrainingDao mapToDao(Training training) {
        return TrainingDaoMapper.toDao(training);
    }
}

