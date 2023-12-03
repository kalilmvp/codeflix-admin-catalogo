package com.fullcycle.admin.catalogo.application.genre.update;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase defaultUpdateGenreUseCase;

    @Mock
    private GenreGateway genreGateway;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCommand_whenCallUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                                                    ,expectedName
                                                    ,expectedIsActive
                                                    ,asString(expectedCategories));

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(this.genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultUpdateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(this.genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName()) &&
                        Objects.equals(expectedIsActive, anUpdatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, anUpdatedGenre.getCategories()) &&
                        Objects.nonNull(anUpdatedGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt()) &&
                        Objects.nonNull(anUpdatedGenre.getUpdatedAt()) &&
                        aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt()) &&
                        Objects.isNull(anUpdatedGenre.getDeletedAt())));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("345")
        );

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(this.genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultUpdateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.genreGateway, times(1)).findById(expectedId);

        verify(this.categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(this.genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName()) &&
                        Objects.equals(expectedIsActive, anUpdatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, anUpdatedGenre.getCategories()) &&
                        Objects.nonNull(anUpdatedGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt()) &&
                        Objects.nonNull(anUpdatedGenre.getUpdatedAt()) &&
                        aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt()) &&
                        Objects.isNull(anUpdatedGenre.getDeletedAt())));
    }

    @Test
    public void givenInvalidNullName_whenCallUpdateGenre_shouldReturnDomainException() {
        // given
        final var aGenre = Genre.newGenre("action", true);
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

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.defaultUpdateGenreUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(this.genreGateway, times(1)).findById(expectedId);
    }

    @Test
    public void givenAnInvalidName_whenCallUpdateGenreAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        // given
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();
        final var movies = CategoryID.from("123");
        final var documentaries = CategoryID.from("456");
        final var series = CategoryID.from("789");
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, documentaries, series);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(List.of(movies));

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.defaultUpdateGenreUseCase.execute(aCommand));

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
        final var aGenre = Genre.newGenre("action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue()
                ,expectedName
                ,expectedIsActive
                ,asString(expectedCategories));

        when(this.genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(this.genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        // when
        final var actualOutput = this.defaultUpdateGenreUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.genreGateway, times(1)).update(argThat(anUpdatedGenre ->
                Objects.equals(expectedName, anUpdatedGenre.getName()) &&
                        Objects.equals(expectedIsActive, anUpdatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, anUpdatedGenre.getCategories()) &&
                        Objects.nonNull(anUpdatedGenre.getId()) &&
                        Objects.nonNull(aGenre.getCreatedAt()) &&
                        Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt()) &&
                        Objects.nonNull(anUpdatedGenre.getUpdatedAt()) &&
                        aGenre.getUpdatedAt().isBefore(anUpdatedGenre.getUpdatedAt()) &&
                        Objects.nonNull(anUpdatedGenre.getDeletedAt())));
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
