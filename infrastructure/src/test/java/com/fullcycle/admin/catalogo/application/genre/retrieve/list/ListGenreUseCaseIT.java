package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 3/11/23 19:42
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase listGenreUseCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidQuery_whenCallListGenres_shouldReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Action", true),
                Genre.newGenre("Adventure", true)
        );

        this.genreRepository.saveAllAndFlush(genres.stream().map(GenreJPAEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = this.listGenreUseCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertTrue(expectedItems.size() == actualResult.items().size() &&
                expectedItems.containsAll(actualResult.items()));
    }

    @Test
    public void givenAnInvalidId_whenHasNoResults_shouldReturnEmptyCategories() {
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, genres.size(), genres);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination;

        final var actualResult = this.listGenreUseCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(genres.size(), actualResult.total());
    }
}
