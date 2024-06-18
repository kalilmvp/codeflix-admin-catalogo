package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;

import java.util.Set;

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
        String direction,
        Set<CategoryID> categories,
        Set<GenreID> genres,
        Set<CastMemberID> castMembers
) {
}
