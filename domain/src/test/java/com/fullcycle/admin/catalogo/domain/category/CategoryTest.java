package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:08
 * @email kalilmvp@gmail.com
 */
public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewAndValidate_thenShouldReturnException() {
        final String expectedName = null;
        final var exceptedErrorCount = 1;
        final var expectedErrorMessdage = "'name' should not be null";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessdage, actualException.getErrors().get(0).message());

    }
}
