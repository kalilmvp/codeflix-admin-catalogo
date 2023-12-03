package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/10/23 19:42
 * @email kalilmvp@gmail.com
 */
public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase defaultGetGenreByIdUseCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.genreGateway);
    }

    @Test
    public void givenAValidId_whenCallGetGenreById_shouldReturnGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"),
                                               CategoryID.from("456"));

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        final var actualGenre = this.defaultGetGenreByIdUseCase.execute(expectedId.getValue());

        assertEquals(GenreOutput.from(aGenre), actualGenre);
        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        verify(this.genreGateway, times(1)).findById(expectedId);
    }

    @Test
    public void givenAValidId_whenCallGetGenreByIdAndDoesNotExist_shouldReturnNotFound() {
        final var expectedId = GenreID.from("123");
        final var exceptedErrorMessage = "Genre with ID 123 was not found";

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> this.defaultGetGenreByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
