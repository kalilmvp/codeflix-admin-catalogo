package com.fullcycle.admin.catalogo.application.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/21/23 22:52
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGatewayMock;

    @Test
    public void givenAValidId_whenCallDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", Boolean.TRUE);

        final var expectedId = aCategory.getId();

        this.save(aCategory);

        assertEquals(1, this.categoryRepository.count());

        assertDoesNotThrow(() -> this.deleteCategoryUseCase.execute(expectedId.getValue()));

        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.from("123");

        assertEquals(0, this.categoryRepository.count());

        assertDoesNotThrow(() -> this.deleteCategoryUseCase.execute(expectedId.getValue()));

        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    public void givenAValidId_wheGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", Boolean.TRUE);

        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway Error")).when(this.categoryGatewayMock).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> this.deleteCategoryUseCase.execute(expectedId.getValue()));

        verify(this.categoryGatewayMock, times(1)).deleteById(eq(expectedId));
    }

    private void save(final Category... aCategory) {
        this.categoryRepository.saveAllAndFlush(Arrays.stream(aCategory)
                .map(CategoryJPAEntity::from)
                .toList());
    }
}
