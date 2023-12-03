package com.fullcycle.admin.catalogo.application.genre.delete;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase defaultDeleteGenreUseCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.genreGateway);
    }

    @Test
    public void givenValidGenreId_whenCallDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();

        doNothing().when(this.genreGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.defaultDeleteGenreUseCase.execute(expectedId.getValue()));

        // then
        verify(this.genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInValidGenreId_whenCallDeleteGenre_shouldBeOk() {
        // given
        final var expectedId = GenreID.from("123");

        doNothing().when(this.genreGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.defaultDeleteGenreUseCase.execute(expectedId.getValue()));

        // then
        verify(this.genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInValidGenreId_whenCallDeleteGenreAnsGatewayThrowsUnexpectedError_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway Error")).when(this.genreGateway).deleteById(any());

        // when
        assertThrows(IllegalStateException.class, () -> this.defaultDeleteGenreUseCase.execute(expectedId.getValue()));

        // then
        verify(this.genreGateway, times(1)).deleteById(expectedId);
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
