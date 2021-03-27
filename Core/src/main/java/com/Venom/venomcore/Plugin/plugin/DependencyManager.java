package com.venom.venomcore.plugin.plugin;

import com.venom.venomcore.plugin.external.dependency.Dependency;
import com.venom.venomcore.plugin.external.dependency.DependencyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyManager {

    private final Map<Dependency, DependencyType> dependencies = new HashMap<>();

    public void addDependency(Dependency dependency, DependencyType type) {
        dependencies.put(dependency, type);
    }

    public List<Dependency> getAllDependencies() {
        return new ArrayList<>(dependencies.keySet());
    }

    public List<Dependency> getDependencies(boolean enabled) {
        return dependencies.keySet()
                .stream()
                .filter(key -> key.isEnabled() == enabled)
                .collect(Collectors.toList());
    }

    public DependencyType getType(Dependency dependency) {
        return dependencies.get(dependency);
    }

    public boolean isDependency(Dependency dependency) {
        return dependencies.containsKey(dependency);
    }

    public List<Dependency> getDependencies(DependencyType type) {
        return dependencies.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == type)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
