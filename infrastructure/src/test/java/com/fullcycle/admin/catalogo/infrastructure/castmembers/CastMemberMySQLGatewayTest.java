package com.fullcycle.admin.catalogo.infrastructure.castmembers;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMembersJPAEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.fullcycle.admin.catalogo.Fixture.CastMember.type;
import static com.fullcycle.admin.catalogo.Fixture.name;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 1/16/24 21:54
 * @email kalilmvp@gmail.com
 */
@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMembersMySQLGateway castMemberMySQLGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testInjectedDependencies() {
        assertNotNull(this.castMemberMySQLGateway);
        assertNotNull(this.castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreateCastMember_shouldPersistCastMember() {
        // given
        final var expectedName = name();
        final var expectedType = type();
        final var aCastMember = CastMember.newCastMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        assertEquals(0, this.castMemberRepository.count());

        // when
        final var actualCastMember = this.castMemberMySQLGateway.create(CastMember.with(aCastMember));

        // then
        assertEquals(1, this.castMemberRepository.count());
        assertEquals(expectedId, actualCastMember.getId());
        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());

        final var persistedGenre = this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.castMemberRepository.count());
        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedType, persistedGenre.getType());
        assertEquals(aCastMember.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), persistedGenre.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateeCastMember_shouldUpdateIt() {
        // given
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;
        final var aCastMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        final var expectedId = aCastMember.getId();

        assertEquals(0, this.castMemberRepository.count());

        final var currentCastMember = this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());
        assertEquals("vind", currentCastMember.getName());
        assertEquals(CastMemberType.DIRECTOR, currentCastMember.getType());

        // when
        final var actualCastMember = this.castMemberMySQLGateway.update(
                CastMember.with(aCastMember).update(expectedName, expectedType));

        // then
        assertEquals(1, this.castMemberRepository.count());
        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertTrue(aCastMember.getUpdatedAt().isBefore(actualCastMember.getUpdatedAt()));

        final var persistedCastMember = this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(1, this.castMemberRepository.count());
        assertEquals(expectedName, persistedCastMember.getName());
        assertEquals(expectedType, persistedCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), persistedCastMember.getCreatedAt());
        assertTrue(aCastMember.getUpdatedAt().isBefore(persistedCastMember.getUpdatedAt()));
    }

    @Test
    public void givenAPrePersistedCastMember_whenCallDeleteById_shouldDeleteCastMember() {
        // given
        CastMember aCastMember = CastMember.newCastMember(name(), type());

        assertEquals(0, this.castMemberRepository.count());

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        // when
        this.castMemberMySQLGateway.deleteById(aCastMember.getId());

        // then
        assertEquals(0, this.castMemberRepository.count());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallDeleteById_shouldReturnOk() {
        // given
        assertEquals(0, this.castMemberRepository.count());

        // when
        this.castMemberMySQLGateway.deleteById(CastMemberID.from("123"));

        // then
        assertEquals(0, this.castMemberRepository.count());
    }

    @Test
    public void givenAPrePersistedCastMember_whenCallFindById_shouldReturnCastMember() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aCastMember = CastMember.newCastMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        // when
        final var actualCastMember = this.castMemberMySQLGateway.findById(expectedId).get();

        // then
        assertEquals(expectedId, actualCastMember.getId());
        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallFindById_shouldReturnEmpty() {
        // given
        final var expectedId = CastMemberID.from("123");

        assertEquals(0, this.castMemberRepository.count());

        // when
        final var actualCastMember = this.castMemberMySQLGateway.findById(expectedId);

        // then
        assertTrue(actualCastMember.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenCallFindAll_shouldReturnEmptyList() {
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
        final var actualPage = this.castMemberMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
        "drew, 0, 10, 1, 1, Drew Barrymore",
        "russo, 0, 10, 1, 1, Russo Brothers",
        "spiel, 0, 10, 1, 1, Steven Spielberg",
        "arn, 0, 10, 1, 1, Arnold Schwarzeneger",
        "ada, 0, 10, 1, 1, Adam Sandler",
    })
    public void givenValidTerms_whenCallFindAll_shouldReturnFiltered(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedName
    ) {
        // given
        mockCastMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.castMemberMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "name, asc, 0, 10, 5, 5, Adam Sandler",
            "name, desc, 0, 10, 5, 5, Steven Spielberg",
            "createdAt, asc, 0, 10, 5, 5, Arnold Schwarzeneger",
            "createdAt, desc, 0, 10, 5, 5, Russo Brothers",
    })
    public void givenValidSortAndDirection_whenCallFindAll_shouldReturnFiltered(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedName
    ) {
        // given
        mockCastMembers();

        final var expectedTerms = "";

        final var aQuery =
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.castMemberMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2, 2, 5, Adam Sandler;Arnold Schwarzeneger",
            "1, 2, 2, 5, Drew Barrymore;Russo Brothers",
            "2, 2, 1, 5, Steven Spielberg",
    })
    public void givenValidPagination_whenCallFindAll_shouldReturnPaginated(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedNames
    ) {
        // given
        mockCastMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
            new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = this.castMemberMySQLGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName: expectedNames.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockCastMembers() {
        this.castMemberRepository.saveAllAndFlush(List.of(
                CastMembersJPAEntity.from(CastMember.newCastMember("Arnold Schwarzeneger", CastMemberType.ACTOR)),
                CastMembersJPAEntity.from(CastMember.newCastMember("Steven Spielberg", CastMemberType.DIRECTOR)),
                CastMembersJPAEntity.from(CastMember.newCastMember("Drew Barrymore", CastMemberType.ACTOR)),
                CastMembersJPAEntity.from(CastMember.newCastMember("Adam Sandler", CastMemberType.ACTOR)),
                CastMembersJPAEntity.from(CastMember.newCastMember("Russo Brothers", CastMemberType.DIRECTOR))));
    }

    ;
}
