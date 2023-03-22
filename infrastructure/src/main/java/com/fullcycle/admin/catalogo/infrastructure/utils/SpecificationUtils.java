package com.fullcycle.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author kalil.peixoto
 * @date 3/18/23 20:23
 * @email kalilmvp@gmail.com
 */
public final class SpecificationUtils {

    private SpecificationUtils() {}

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), like(term.toUpperCase()));
    }

    private static String like(final String term) {
        return "%" + term + "%";
    }
}
