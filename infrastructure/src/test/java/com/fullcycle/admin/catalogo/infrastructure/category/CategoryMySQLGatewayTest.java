package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.pagination.CategorySearchQuery;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 3/16/23 22:05
 * @email kalilmvp@gmail.com
 */
@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjectedDependencies() {
        assertNotNull(this.categoryMySQLGateway);
        assertNotNull(this.categoryRepository);
    }

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes Description";
        final var isActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

        assertEquals(0, this.categoryRepository.count());

        final var actualCategory = this.categoryMySQLGateway.create(aCategory);

        assertEquals(1, this.categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(isActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());

        final var aCategoryEntity = this.categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), aCategoryEntity.getId());
        assertEquals(expectedName, aCategoryEntity.getName());
        assertEquals(expectedDescription, aCategoryEntity.getDescription());
        assertEquals(isActive, aCategoryEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), aCategoryEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), aCategoryEntity.getUpdatedAt());
        assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnACategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes Description";
        final var isActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, null, isActive);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        assertEquals(1, this.categoryRepository.count());

        final var actualInvalidEntity = this.categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(expectedName, actualInvalidEntity.getName());
        assertEquals(null, actualInvalidEntity.getDescription());
        assertEquals(isActive, actualInvalidEntity.isActive());

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, isActive);

        final var actualCategory = this.categoryMySQLGateway.update(anUpdatedCategory);

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(isActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());

        final var aCategoryEntity = this.categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), aCategoryEntity.getId());
        assertEquals(expectedName, aCategoryEntity.getName());
        assertEquals(expectedDescription, aCategoryEntity.getDescription());
        assertEquals(isActive, aCategoryEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), aCategoryEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(aCategoryEntity.getUpdatedAt()));
        assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryingToDelete_shouldDeleteCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes Description";
        final var isActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        assertEquals(1, this.categoryRepository.count());

        this.categoryMySQLGateway.deleteById(aCategory.getId());

        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    public void givenAnInValidCategoryId_whenTryingToDelete_shouldNotDeleteCategory() {
        assertEquals(0, this.categoryRepository.count());

        this.categoryMySQLGateway.deleteById(CategoryID.from("invalid-id"));

        assertEquals(0, this.categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategory_whenCallsFindBtId_shouldReturnACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes Description";
        final var isActive = Boolean.TRUE;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        assertEquals(1, this.categoryRepository.count());

        final var actualCategory = this.categoryMySQLGateway.findById(aCategory.getId()).get();

        assertEquals(1, this.categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(isActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdNotStore_whenCallsFindBtId_shouldReturnEmpty() {
        assertEquals(0, this.categoryRepository.count());

        final var actualCategory = this.categoryMySQLGateway.findById(CategoryID.from("some_id"));

        assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, Boolean.TRUE);
        final var series = Category.newCategory("Séries", null, Boolean.TRUE);
        final var documentarios = Category.newCategory("Documentários", null, Boolean.TRUE);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAll(Arrays.asList(CategoryJPAEntity.from(filmes), CategoryJPAEntity.from(series), CategoryJPAEntity.from(documentarios)));

        assertEquals(expectedTotal, this.categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "",  "name", "asc");

        final var actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, this.categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "",  "name", "asc");

        final var actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, Boolean.TRUE);
        final var series = Category.newCategory("Séries", null, Boolean.TRUE);
        final var documentarios = Category.newCategory("Documentários", null, Boolean.TRUE);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAll(Arrays.asList(CategoryJPAEntity.from(filmes), CategoryJPAEntity.from(series), CategoryJPAEntity.from(documentarios)));

        assertEquals(expectedTotal, this.categoryRepository.count());

        var aQuery = new CategorySearchQuery(0, 1, "",  "name", "asc");
        var actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

        //Page 1
        expectedPage = 1;
        aQuery = new CategorySearchQuery(1, 1, "",  "name", "asc");
        actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(filmes.getId(), actualResult.items().get(0).getId());

        //Page 2
        expectedPage = 2;
        aQuery = new CategorySearchQuery(2, 1, "",  "name", "asc");
        actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, Boolean.TRUE);
        final var series = Category.newCategory("Séries", null, Boolean.TRUE);
        final var documentarios = Category.newCategory("Documentários", null, Boolean.TRUE);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAll(Arrays.asList(CategoryJPAEntity.from(filmes), CategoryJPAEntity.from(series), CategoryJPAEntity.from(documentarios)));

        assertEquals(3, this.categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "doc",  "name", "asc");

        final var actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidas_whenCallsFindAllAndTermsMatchCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", Boolean.TRUE);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", Boolean.TRUE);
        final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", Boolean.TRUE);

        assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.saveAll(Arrays.asList(CategoryJPAEntity.from(filmes), CategoryJPAEntity.from(series), CategoryJPAEntity.from(documentarios)));

        assertEquals(3, this.categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA",  "name", "asc");

        final var actualResult = this.categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(filmes.getId(), actualResult.items().get(0).getId());
    }
}
