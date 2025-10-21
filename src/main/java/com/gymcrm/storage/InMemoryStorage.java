package com.gymcrm.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alish
 */
@Component
public class InMemoryStorage {
    private final Map<String, Object> storage = new HashMap<>();

    public Map<String, Object> getNamespace(String name) {
        return (Map<String, Object>) storage.computeIfAbsent(name, k -> new HashMap<>());
    }
}
