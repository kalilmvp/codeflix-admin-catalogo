package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author kalil.peixoto
 * @date 3/3/24 11:49
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase createGenreUseCase;
    @SpyBean
    private CategoryGateway categoryGateway;
    @SpyBean
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidCommand_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var movies = this.categoryGateway.create(Category.newCategory("Movies", "", true));
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = this.createGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(actualOutput.id()).get();
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithoutCategories_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = this.createGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(actualOutput.id()).get();
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = this.createGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = this.genreRepository.findById(actualOutput.id()).get();
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidEmptyName_whenCallCreateGenre_shouldReturnDomainException() {
        // given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidNullName_whenCallCreateGenre_shouldReturnDomainException() {
        // given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        // given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var movies = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series.getId(), documentaries);

        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.categoryGateway, times(1)).existsByIds(any());
        verify(this.genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidName_whenCallCreateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        // given
        final var series =
                categoryGateway.create(Category.newCategory("Series", null, true));

        final var movies = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series.getId(), documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.categoryGateway, times(1)).existsByIds(any());
        verify(this.genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
