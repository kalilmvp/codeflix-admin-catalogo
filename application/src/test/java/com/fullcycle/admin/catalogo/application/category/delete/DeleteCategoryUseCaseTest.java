package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 03/11/23 10:43
 * @email kalilmvp@gmail.com
 */
@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase defaultDeleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGatewayMock;

    @BeforeEach
    void cleanUp() {
        reset(this.categoryGatewayMock);
    }

    // 1. Teste do Caminho Feliz
    // 2. Teste passando o ID errado, também deve retornar ok
    // 3. Teste lançando erro e capturando

    @Test
    public void givenAValidId_whenCallDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", Boolean.TRUE);

        final var expectedId = aCategory.getId();

        doNothing().when(this.categoryGatewayMock).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> this.defaultDeleteCategoryUseCase.execute(expectedId.getValue()));

        verify(this.categoryGatewayMock, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.from("123");

        doNothing().when(this.categoryGatewayMock).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> this.defaultDeleteCategoryUseCase.execute(expectedId.getValue()));

        verify(this.categoryGatewayMock, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_wheGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", Boolean.TRUE);

        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway Error")).when(this.categoryGatewayMock).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> this.defaultDeleteCategoryUseCase.execute(expectedId.getValue()));

        verify(this.categoryGatewayMock, times(1)).deleteById(eq(expectedId));
    }
}
