package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
public class CreateGenreUseCaseTest extends UseCaseTest  {

    @InjectMocks
    private DefaultCreateGenreUseCase defaultCreateGenreUseCase;

    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.genreGateway, this.categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultCreateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName()) &&
                        Objects.equals(expectedIsActive, aGenre.isActive()) &&
                        Objects.equals(expectedCategories, aGenre.getCategories()) &&
                        Objects.nonNull(aGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.nonNull(aGenre.getUpdatedAt()) &&
                        Objects.isNull(aGenre.getDeletedAt())));
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultCreateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName()) &&
                        Objects.equals(expectedIsActive, aGenre.isActive()) &&
                        Objects.equals(expectedCategories, aGenre.getCategories()) &&
                        Objects.nonNull(aGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.nonNull(aGenre.getUpdatedAt()) &&
                        Objects.nonNull(aGenre.getDeletedAt())));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("345")
        );

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultCreateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(this.genreGateway, times(1)).create(argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName()) &&
                        Objects.equals(expectedIsActive, aGenre.isActive()) &&
                        Objects.equals(expectedCategories, aGenre.getCategories()) &&
                        Objects.nonNull(aGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.nonNull(aGenre.getUpdatedAt()) &&
                        Objects.isNull(aGenre.getDeletedAt())));
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
        final var actualException = assertThrows(NotificationException.class, () -> defaultCreateGenreUseCase.execute(aCommand));

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
        final var actualException = assertThrows(NotificationException.class, () -> defaultCreateGenreUseCase.execute(aCommand));

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
        final var movies = CategoryID.from("123");
        final var documentaries = CategoryID.from("456");
        final var series = CategoryID.from("789");
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, documentaries, series);

        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(List.of(movies));

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> defaultCreateGenreUseCase.execute(aCommand));

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
        final var movies = CategoryID.from("123");
        final var documentaries = CategoryID.from("456");
        final var series = CategoryID.from("789");
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, documentaries, series);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(List.of(movies));

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> defaultCreateGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.categoryGateway, times(1)).existsByIds(any());
        verify(this.genreGateway, times(0)).create(any());
    }
}
