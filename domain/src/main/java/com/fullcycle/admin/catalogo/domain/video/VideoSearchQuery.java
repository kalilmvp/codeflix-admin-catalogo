package com.fullcycle.admin.catalogo.domain.video;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:16
 * @email kalilmvp@gmail.com
 */
public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
