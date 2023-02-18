package com.fullcycle.admin.catalogo.domain.category.pagination;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:16
 * @email kalilmvp@gmail.com
 */
public record CategorySearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
