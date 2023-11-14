package com.fullcycle.admin.catalogo.application.update;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/21/23 22:52
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGatewayMock;

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Filme", null, true);

        assertEquals(0, this.categoryRepository.count());

        this.save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        assertEquals(1, this.categoryRepository.count());

        final var actualOutput = this.updateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = this.categoryRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallUpdateCategory_shouldReturnDomainException() {
        final var aCategory = Category.newCategory("Filme", null, true);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        this.save(aCategory);

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);


        final var notification = this.updateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(this.categoryGatewayMock, times(0)).update(any());
    }

    @Test
    public void givenAValidInnactiveCommand_whenCallUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Filme", null, true);

        this.save(aCategory);

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.FALSE;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = this.updateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = this.categoryRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("Filme", null, true);

        this.save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(this.categoryGatewayMock)
                .update(any());

        final var notification = this.updateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory = this.categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidId_whenCallUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.FALSE;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> this.updateCategoryUseCase.execute(aCommand));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    private void save(final Category... aCategory) {
        this.categoryRepository.saveAllAndFlush(Arrays.stream(aCategory)
                .map(CategoryJPAEntity::from)
                .toList());
    }
}
