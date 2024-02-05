package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:13
 * @email kalilmvp@gmail.com
 */
public interface GenreGateway {

    Genre create(Genre aGenre);
    void deleteById(GenreID anId);
    Optional<Genre> findById(GenreID anId);
    Genre update(Genre aGenre);
    Pagination<Genre> findAll(SearchQuery aQuery);
}
