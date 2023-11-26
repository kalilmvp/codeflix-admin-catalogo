package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author kalil.peixoto
 * @date 11/21/23 14:51
 * @email kalilmvp@gmail.com
 */
@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withUsername("root")
                    .withPassword("123456")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = this.givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveACategory(actualId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        this.givenACategory("Movies", null, true);
        this.givenACategory("Documentários", null, true);
        this.givenACategory("Séries", null, true);

        this.listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")));

        this.listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));

        this.listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Séries")));

        this.listCategories(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        this.givenACategory("Movies", null, true);
        this.givenACategory("Documentários", null, true);
        this.givenACategory("Séries", null, true);

        this.listCategories(0, 1, "mov")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Movies")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        this.givenACategory("Movies", "A", true);
        this.givenACategory("Documentários", "B", true);
        this.givenACategory("Séries", "C", true);

        this.listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Séries")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Documentários")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Movies")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = this.givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = this.categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        final var aRequest = get("/categories/123");

        this.mockMvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        final var actualId = this.givenACategory("Filmes", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = put("/categories/".concat(actualId.getValue()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mockMvc.perform(aRequest)
                        .andExpect(status().isOk());

        final var actualCategory = this.categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualId = this.givenACategory("Filmes", null, true);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = put("/categories/".concat(actualId.getValue()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mockMvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = this.categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = this.givenACategory("Filmes", null, false);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var aRequest = put("/categories/".concat(actualId.getValue()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        this.mockMvc.perform(aRequest)
                .andExpect(status().isOk());

        final var actualCategory = this.categoryRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.categoryRepository.count());

        final var actualId = this.givenACategory("Filmes", "A categoria mais assistida", true);

        this.mockMvc.perform(
                delete("/categories/".concat(actualId.getValue()))
        );

        assertFalse(this.categoryRepository.existsById(actualId.getValue()));
    }


    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return this.listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return this.listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String dir) throws Exception {
        final var aRequest = get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", dir)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(aRequest);
    }

    private CategoryResponse retrieveACategory(final String anId) throws Exception {
        final var aRequest = get("/categories/" + anId);

        final var json = this.mockMvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    private CategoryID givenACategory(final String aName, String aDescription, final boolean aIsActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, aIsActive);

        final var aRequest = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }
}
