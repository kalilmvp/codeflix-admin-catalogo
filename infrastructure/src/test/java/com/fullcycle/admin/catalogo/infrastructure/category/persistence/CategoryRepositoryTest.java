package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 3/17/23 09:31
 * @email kalilmvp@gmail.com
 */
@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidName_whenCallsSave_shouldReturnError() {
        final var propertyName = "name";
        final var actualCauseMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.name";
        final var aCategory = Category.newCategory("Filmes", "Filmes description", Boolean.TRUE);

        final var aCategoryEntity = CategoryJPAEntity.from(aCategory);
        aCategoryEntity.setName(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(aCategoryEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(actualCauseMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var propertyName = "createdAt";
        final var actualCauseMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.createdAt";
        final var aCategory = Category.newCategory("Filmes", "Filmes description", Boolean.TRUE);

        final var aCategoryEntity = CategoryJPAEntity.from(aCategory);
        aCategoryEntity.setCreatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(aCategoryEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(actualCauseMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdateddAt_whenCallsSave_shouldReturnError() {
        final var propertyName = "updatedAt";
        final var actualCauseMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.updatedAt";
        final var aCategory = Category.newCategory("Filmes", "Filmes description", Boolean.TRUE);

        final var aCategoryEntity = CategoryJPAEntity.from(aCategory);
        aCategoryEntity.setUpdatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> this.categoryRepository.save(aCategoryEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(actualCauseMessage, actualCause.getMessage());
    }
}
