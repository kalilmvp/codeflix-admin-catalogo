package com.fullcycle.admin.catalogo.application.genre.update;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase updateGenreUseCase;
    @SpyBean
    private GenreGateway genreGateway;
    @SpyBean
    private CategoryGateway categoryGateway;
    @SpyBean
    private GenreRepository genreRepository;

    @Test
    public void givenValidCommand_whenCallUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                                                    ,expectedName
                                                    ,expectedIsActive
                                                    ,asString(expectedCategories));

        // when
        final var actualOutput = this.updateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = this.genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
            expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithCategories_whenCallUpdateGenre_shouldReturnGenreId() {
        // given
        final var movies = this.categoryGateway.create(Category.newCategory("movies", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("series", null, true));
        final var documentaries = this.categoryGateway.create(Category.newCategory("documentaries", null, true));

        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId(), documentaries.getId());

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        // when
        final var actualOutput = this.updateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallUpdateGenre_shouldReturnDomainException() {
        // given
        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.updateGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAnInvalidName_whenCallUpdateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        // given
        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();
        final var movies = this.categoryGateway.create(Category.newCategory("movies", null, true));
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series, documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.updateGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.genreGateway, times(1)).findById(expectedId);
        verify(this.categoryGateway, times(1)).existsByIds(any());
        verify(this.genreGateway, times(0)).update(any());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = this.genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        // when
        final var actualOutput = this.updateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
