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
    public void givenAnInvalidNullName_whenCallNewAndValidate_thenShouldReturnError() {
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

    @Test
    public void givenAnInvalidEmptyName_whenCallNewAndValidate_thenShouldReturnException() {
        final String expectedName = "";
        final var exceptedErrorCount = 1;
        final var expectedErrorMessdage = "'name' should not be empty";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessdage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLenghtLessThan3_whenCallNewAndValidate_thenShouldReturnException() {
        final String expectedName = "Fi ";
        final var exceptedErrorCount = 1;
        final var expectedErrorMessdage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessdage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLenghtGreaterThan255_whenCallNewAndValidate_thenShouldReturnException() {
        final String expectedName = """
                Pensando mais a longo prazo, a estrutura atual da organização obstaculiza a apreciação da importância das novas proposições. Ainda assim, existem dúvidas a respeito de como a necessidade de renovação processual
                 auxilia a preparação e a composição de alternativas às soluções ortodoxas. Neste sentido, a consolidação das estruturas assume importantes posições no estabelecimento das direções preferenciais no sentido do 
                 progresso. Do mesmo modo, o desafiador cenário globalizado facilita a criação do sistema de formação de quadros que corresponde às necessidades. 
                As experiências acumuladas demonstram que a consulta aos diversos militantes garante a contribuição de um grupo importante na determinação das condições inegavelmente apropriadas.
                """;
        final var exceptedErrorCount = 1;
        final var expectedErrorMessdage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(exceptedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessdage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewAndValidate_thenShouldNotReturnException() {
        final String expectedName = "Movie";
        final var expectedDescription = " ";
        final var expectedIsActive = Boolean.TRUE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
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
    public void givenAValidFalseIsActive_whenCallNewAndValidate_thenShouldNotReturnException() {
        final String expectedName = "Movie";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.FALSE;

        final var actualCategory = Category.newCategory(expectedName,
                expectedDescription,
                expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenShouldReturnCategoryDeactivated() {
        final String expectedName = "Movie";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.FALSE;

        final var aCategory = Category.newCategory(expectedName,
                expectedDescription,
                Boolean.TRUE);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.deactivate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenShouldReturnCategoryActivated() {
        final String expectedName = "Movie";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName,
                expectedDescription,
                Boolean.FALSE);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final String expectedName = "Movie";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var aCategory = Category.newCategory("ABCD",
                "Description",
                expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.updated(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final String expectedName = "Movie";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.FALSE;

        final var aCategory = Category.newCategory("ABCD",
                "Description",
                Boolean.TRUE);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.updated(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = Boolean.TRUE;

        final var aCategory = Category.newCategory("ABCD",
                "Description",
                expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.updated(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());
    }
}
