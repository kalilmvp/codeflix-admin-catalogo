package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

/**
 * @author kalil.peixoto
 * @date 3/12/23 08:28
 * @email kalilmvp@gmail.com
 */
public record GenreListOutput(String id,
                              String name,
                              List<String> genres,
                              boolean isActive,
                              Instant createdAt,
                              Instant deletedAt) {

    public static GenreListOutput from(Genre aGenre) {
        return new GenreListOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt());
    }
}
