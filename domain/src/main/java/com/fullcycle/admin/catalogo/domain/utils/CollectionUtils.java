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

    public static <IN, OUT> Set<OUT> mapTo(Set<IN> ids, Function<IN, OUT> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}
