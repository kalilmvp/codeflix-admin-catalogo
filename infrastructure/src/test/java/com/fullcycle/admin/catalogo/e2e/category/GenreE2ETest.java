package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author kalil.peixoto
 * @date 3/21/24 09:25
 * @email kalilmvp@gmail.com
 */
@E2ETest
@Testcontainers
public class GenreE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GenreRepository genreRepository;

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

    private GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));
        return GenreID.from(
                this.mockMvc.perform(aRequest)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse().getHeader("Location")
                        .replace("/genres/", ""));
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

    private <A, D> List<D> mapTo(final List<A> actual, Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }
}
