package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/4/24 09:59
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class GetGenreByIdUseCaseIT {
    @Autowired
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    public void givenAValidId_whenCallGetGenreById_shouldReturnGenre() {
        final var movies = this.categoryGateway.create(Category.newCategory("Movies", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("Series", null, true));
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = this.genreGateway.create(Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories));

        final var expectedId = aGenre.getId();

        final var actualGenre = this.getGenreByIdUseCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.categories().size() &&
                asString(expectedCategories).containsAll(actualGenre.categories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenAValidId_whenCallGetGenreAndDoesNotExist_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        final var actualException = assertThrows(NotFoundException.class, () -> {
            this.getGenreByIdUseCase.execute(expectedId.getValue());
        });

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(final List<? extends Identifier> values) {
        return values.stream()
                .map(Identifier::getValue)
                .collect(Collectors.toList());
    }
}
