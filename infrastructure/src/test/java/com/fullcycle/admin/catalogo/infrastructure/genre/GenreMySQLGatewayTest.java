package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 1/16/24 21:54
 * @email kalilmvp@gmail.com
 */
@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;
  
    @Autowired
    private GenreMySQLGateway genreMySQLGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testInjectedDependencies() {
        assertNotNull(this.categoryMySQLGateway);
        assertNotNull(this.genreMySQLGateway);
        assertNotNull(this.genreRepository);
    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var movies = this.categoryMySQLGateway.create(Category.newCategory("Movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        final var actualGenre = this.genreMySQLGateway.create(aGenre);

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategory_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        final var actualGenre = this.genreMySQLGateway.create(aGenre);

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertEquals(1, this.genreRepository.count());
        assertEquals("ac", aGenre.getName());
        assertEquals(0, aGenre.getCategories().size());

        final var actualGenre = this.genreMySQLGateway.update(
                Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertIterableEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleanCategories_shouldPersistGenre() {
        final var movies = this.categoryMySQLGateway.create(Category.newCategory("Movies", null, true));
        final var series = this.categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(movies.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertEquals(1, this.genreRepository.count());
        assertEquals("ac", aGenre.getName());
        assertEquals(2, aGenre.getCategories().size());

        final var actualGenre = this.genreMySQLGateway.update(
                Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertFalse(aGenre.isActive());
        assertNotNull(aGenre.getDeletedAt());

        final var actualGenre = this.genreMySQLGateway.update(
                Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreDeactivating_shouldPersistGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualGenre = this.genreMySQLGateway.update(
                Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = this.genreRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.genreRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallDeleteById_shouldDeleteGenre() {
        // given
        Genre aGenre = Genre.newGenre("Action", true);

        assertEquals(0, this.genreRepository.count());

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertEquals(1, this.genreRepository.count());

        // when
        this.genreMySQLGateway.deleteById(aGenre.getId());

        // then
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallDeleteById_shouldReturnOk() {
        // given
        assertEquals(0, this.genreRepository.count());

        // when
        this.genreMySQLGateway.deleteById(GenreID.from("123"));

        // then
        assertEquals(0, this.genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallFindById_shouldReturnGenre() {
        // given
        final var movies = this.categoryMySQLGateway.create(Category.newCategory("Movies", null, true));
        final var series = this.categoryMySQLGateway.create(Category.newCategory("Series", null, true));
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        this.genreRepository.saveAndFlush(GenreJPAEntity.from(aGenre));

        assertEquals(1, this.genreRepository.count());

        // when
        final var actualGenre = this.genreMySQLGateway.findById(expectedId).get();

        // then
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidGenre_whenCallFindById_shouldReturnEmpty() {
        // given
        final var expectedId = GenreID.from("123");

        assertEquals(0, this.genreRepository.count());

        // when
        final var actualGenre = this.genreMySQLGateway.findById(expectedId);

        // then
        assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = 
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.genreMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
        "ac, 0, 10, 1, 1, Action",
        "dr, 0, 10, 1, 1, Drama",
        "com, 0, 10, 1, 1, Romantic Comedy",
        "cien, 0, 10, 1, 1, Science Fiction",
        "ter, 0, 10, 1, 1, Terror",
    })
    public void givenValidTerms_whenCallFindAll_shouldReturnFiltered(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedGenreName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        // given
        mockGenres();

        final var aQuery = 
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.genreMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
        
    }

    @ParameterizedTest
    @CsvSource({
        "name, asc, 0, 10, 5, 5, Action",
        "name, desc, 0, 10, 5, 5, Terror",
        "createdAt, asc, 0, 10, 5, 5, Romantic Comedy",
        "createdAt, desc, 0, 10, 5, 5, Science Fiction"
    })
    public void givenValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedGenreName
    ) {
        // given
        mockGenres();

        final var expectedTerms = "";

        final var aQuery = 
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.genreMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
        "0, 2, 2, 5, Action;Drama",
        "1, 2, 2, 5, Romantic Comedy;Science Fiction",
        "2, 2, 1, 5, Terror"
    })
    public void givenValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedGenres
    ) {
        // given
        mockGenres();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = 
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.genreMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName: expectedGenres.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockGenres() {
       this.genreRepository.saveAllAndFlush(List.of(
                                            GenreJPAEntity.from(Genre.newGenre("Romantic Comedy", true)),
                                            GenreJPAEntity.from(Genre.newGenre("Action", true)),
                                            GenreJPAEntity.from(Genre.newGenre("Drama", true)),
                                            GenreJPAEntity.from(Genre.newGenre("Terror", true)),
                                            GenreJPAEntity.from(Genre.newGenre("Science Fiction", true))));
    };

    private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
        return expectedCategories.stream()
                    .sorted(Comparator.comparing(CategoryID::getValue))
                    .toList();
    }   
}
