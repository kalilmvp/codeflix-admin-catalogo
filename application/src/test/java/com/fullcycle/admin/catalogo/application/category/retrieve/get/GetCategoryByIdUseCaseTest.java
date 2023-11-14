package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/10/23 19:42
 * @email kalilmvp@gmail.com
 */
@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase defaultGetCategoryByIdUseCase;

    @Mock
    private CategoryGateway categoryGatewayMock;

    @BeforeEach
    void cleanUp() {
        reset(this.categoryGatewayMock);
    }

    @Test
    public void givenAValidId_whenCallGetCategoryById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = this.defaultGetCategoryByIdUseCase.execute(expectedId.getValue());

        assertEquals(CategoryOutput.from(aCategory), actualCategory);
        assertEquals(expectedId, actualCategory.id());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
    }

    @Test
    public void givenAnInvalidId_whenCallGetCategoryById_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var exceptedErrorMessage = "Category with ID 123 was not found";

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> this.defaultGetCategoryByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_wheGatewayThrowsError_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var exceptedErrorMessage = "Gateway Error";

        when(this.categoryGatewayMock.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(exceptedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> this.defaultGetCategoryByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }
}
