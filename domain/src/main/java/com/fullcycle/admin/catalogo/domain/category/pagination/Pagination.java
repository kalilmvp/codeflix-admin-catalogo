package com.fullcycle.admin.catalogo.domain.category.pagination;

import java.util.List;

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
}
