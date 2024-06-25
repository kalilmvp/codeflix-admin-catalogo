package com.fullcycle.admin.catalogo.infrastructure.services;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 6/25/24 08:16
 * @email kalilmvp@gmail.com
 */
public interface StorageService {
    void deleteAll(Collection<String> names);
    Optional<Resource> get(String name);
    List<String> list(String prefix);
    void store(String name, Resource resource);
}
