package com.fullcycle.admin.catalogo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author kalil.peixoto
 * @date 11/29/23 22:52
 * @email kalilmvp@gmail.com
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <IN, OUT> Set<OUT> mapTo(Set<IN> list, Function<IN, OUT> mapper) {
        if (list == null) return null;

        return list.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}
