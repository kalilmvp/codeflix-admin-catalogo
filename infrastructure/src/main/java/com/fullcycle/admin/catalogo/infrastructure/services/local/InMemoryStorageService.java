package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kalil.peixoto
 * @date 6/25/24 08:19
 * @email kalilmvp@gmail.com
 */
public class InMemoryStorageService implements StorageService {
    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public void reset() {
        this.storage.clear();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(this.storage.get(name));
    }

    @Override
    public List<String> list(final String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet()
                .stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(final String name, final Resource resource) {
        this.storage.put(name, resource);
    }
}
