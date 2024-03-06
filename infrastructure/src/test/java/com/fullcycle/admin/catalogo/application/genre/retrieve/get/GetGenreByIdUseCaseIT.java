package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        assertEquals(GenreOutput.from(aGenre), actualGenre);
        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.categories().size());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
    }
}
