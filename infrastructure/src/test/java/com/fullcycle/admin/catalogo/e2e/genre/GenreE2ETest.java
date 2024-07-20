package com.fullcycle.admin.catalogo.e2e.genre;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author kalil.peixoto
 * @date 3/21/24 09:25
 * @email kalilmvp@gmail.com
 */
@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer(MYSQL_VERSION)
                    .withUsername("root")
                    .withPassword("123456")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mock() {
        return this.mockMvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.genreRepository.count());

        final var expectedName = "Action";
        final var expectedCategories = List.<CategoryID>of();
        final var expectedIsActive = true;

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.genreRepository.count());

        final var expectedName = "Action";
        final var expectedCategories = List.of(this.givenACategory("Movies", null, true));
        final var expectedIsActive = true;

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Action", true, List.of());
        this.givenAGenre("Drama", true, List.of());
        this.givenAGenre("Terror", true, List.of());

        this.listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        this.listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        this.listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Terror")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Action", true, List.of());
        this.givenAGenre("Drama", true, List.of());
        this.givenAGenre("Terror", true, List.of());

        this.listGenres(0, 1, "dr")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Action", true, List.of());
        this.givenAGenre("Drama", true, List.of());
        this.givenAGenre("Terror", true, List.of());

        this.listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Terror")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Action")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.genreRepository.count());

        final var movies = this.givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.retrieveAGenre(actualId);

        assertEquals(expectedName, actualGenre.name());
        assertTrue(expectedCategories.size() == actualGenre.categories().size() &&
                mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories()));
        assertEquals(expectedIsActive, actualGenre.active());
        assertNotNull(actualGenre.createdAt());
        assertNotNull(actualGenre.updatedAt());
        assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        final var aRequest = get("/genres/123");

        this.mockMvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        final var movies = this.givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        final var movies = this.givenACategory("Movies", null, true);

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var actualId = this.givenAGenre(expectedName, true, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size() &&
                expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = this.givenAGenre(expectedName, false, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategoryIDs());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        final var movies = this.givenACategory("Movies", null, true);

        final var actualId = this.givenAGenre("Action", true, List.of(movies));

        this.deleteAGenre(actualId)
                .andExpect(status().isNoContent());

        assertFalse(this.genreRepository.existsById(actualId.getValue()));
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    public void asACatalogAdminIShouldNotSeeErrorByDeletingANotExistingGenre() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, this.genreRepository.count());

        this.deleteAGenre(GenreID.from("123121212"))
                .andExpect(status().isNoContent());

        assertEquals(0, this.genreRepository.count());
    }
}
