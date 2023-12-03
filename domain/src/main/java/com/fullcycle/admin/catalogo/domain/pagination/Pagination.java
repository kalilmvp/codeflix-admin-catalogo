package com.fullcycle.admin.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:15
 * @email kalilmvp@gmail.com
 */
public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        List<R> aNewResult = this.items.stream().map(mapper).toList();
        return new Pagination<>(currentPage(), perPage(), total(), aNewResult);
    }
}
