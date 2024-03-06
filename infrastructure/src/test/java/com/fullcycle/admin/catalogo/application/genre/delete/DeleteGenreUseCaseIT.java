package com.fullcycle.admin.catalogo.application.genre.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase deleteGenreUseCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidGenreId_whenCallDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();

        assertEquals(1, this.genreRepository.count());

        // when
        assertDoesNotThrow(() -> this.deleteGenreUseCase.execute(expectedId.getValue()));

        assertEquals(0, this.genreRepository.count());
    }

    @Test
    public void givenInValidGenreId_whenCallDeleteGenre_shouldBeOk() {
        this.genreGateway.create(Genre.newGenre("action", true));

        assertEquals(1, this.genreRepository.count());

        // given
        final var expectedId = GenreID.from("123");

        assertEquals(1, this.genreRepository.count());

        // when
        assertDoesNotThrow(() -> this.deleteGenreUseCase.execute(expectedId.getValue()));

        assertEquals(1, this.genreRepository.count());
    }
}
