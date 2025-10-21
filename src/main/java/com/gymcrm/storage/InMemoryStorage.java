package com.gymcrm.storage;

import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
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
        namespaces.put("trainees", new HashMap<String, Trainee>());
        namespaces.put("trainers", new HashMap<String, Trainer>());
        namespaces.put("trainings", new HashMap<Long, Training>());
    }

    public <K, V> Map<K, V> getNamespace(String name) {
        return (Map<K, V>) namespaces.get(name);
    }
}
