package com.fullcycle.admin.catalogo.application.retrieve.get;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/21/23 22:52
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGatewayMock;

    @Test
    public void givenAValidId_whenCallGetCategoryById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        this.save(aCategory);

        final var actualCategory = this.getCategoryByIdUseCase.execute(expectedId.getValue());

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

        final var actualException = assertThrows(DomainException.class, () ->
                this.getCategoryByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_wheGatewayThrowsError_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var exceptedErrorMessage = "Gateway Error";

        doThrow(new IllegalStateException(exceptedErrorMessage))
                .when(this.categoryGatewayMock)
                .findById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> this.getCategoryByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        this.categoryRepository.saveAllAndFlush(Arrays.stream(aCategory)
                .map(CategoryJPAEntity::from)
                .toList());
    }
}
