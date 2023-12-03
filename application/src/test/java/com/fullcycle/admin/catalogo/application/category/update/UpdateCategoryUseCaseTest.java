package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/10/23 19:42
 * @email kalilmvp@gmail.com
 */
public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase defaultUpdateCategoryUseCase;

    @Mock
    private CategoryGateway categoryGatewayMock;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGatewayMock);
    }

    // 1. Teste do Caminho Feliz
    // 2. Teste passando uma propriedade inválida
    // 3. Teste atualizando uma categoria para inativa
    // 4. Teste simulando um erro genérico vindo do gateway
    // 5. Teste atualizar categoria passando um ID inválido

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Filme", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(this.categoryGatewayMock.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = this.defaultUpdateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.categoryGatewayMock, times(1)).findById(eq(expectedId));

        verify(this.categoryGatewayMock, times(1)).update(argThat(aUpdatedCategory -> Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
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

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                                                        expectedName,
                                                        expectedDescription,
                                                        expectedIsActive);

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var notification = this.defaultUpdateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(this.categoryGatewayMock, times(0)).update(any());
    }

    @Test
    public void givenAValidInnactiveCommand_whenCallUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Filme", null, true);

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.FALSE;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(this.categoryGatewayMock.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = this.defaultUpdateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.categoryGatewayMock, times(1)).findById(eq(expectedId));

        verify(this.categoryGatewayMock, times(1)).update(argThat(aUpdatedCategory -> Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.nonNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("Filme", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(this.categoryGatewayMock.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = this.defaultUpdateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(this.categoryGatewayMock, times(1)).update(argThat(aUpdatedCategory -> Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.FALSE;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGatewayMock.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> this.defaultUpdateCategoryUseCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.categoryGatewayMock, times(1)).findById(eq(CategoryID.from(expectedId)));

        verify(this.categoryGatewayMock, times(0)).update(any());
    }
}
