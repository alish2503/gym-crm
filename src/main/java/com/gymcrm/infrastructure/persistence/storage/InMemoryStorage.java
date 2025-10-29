package com.gymcrm.infrastructure.persistence.storage;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alish
 */
@Component
public class InMemoryStorage {
    private final Map<String, Map<?, ?>> namespaces = new HashMap<>();

    public InMemoryStorage() {
        namespaces.put("trainees", new HashMap<String, TraineeDao>());
        namespaces.put("trainers", new HashMap<String, TrainerDao>());
        namespaces.put("trainings", new HashMap<Long, TrainingDao>());
        namespaces.put("trainingTypes", createTrainingTypesNamespace());
    }

    public <K, V> Map<K, V> getNamespace(String name) {
        return (Map<K, V>) namespaces.get(name);
    }

    private Map<Long, TrainingTypeDao> createTrainingTypesNamespace() {
        Map<Long, TrainingTypeDao> map = new HashMap<>();
        Long id = 1L;
        for (TrainingTypeEnum type : TrainingTypeEnum.values()) {
            map.put(++id, new TrainingTypeDao(id, type.name()));
        }
        return map;
    }
}
