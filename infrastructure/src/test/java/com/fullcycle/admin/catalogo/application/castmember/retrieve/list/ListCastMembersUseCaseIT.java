package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMembersJPAEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/11/23 19:42
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class ListCastMembersUseCaseIT {

    @Autowired
    private ListCastMembersUseCase listCastMembersUseCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidQuery_whenCallListCastMembers_shouldReturnCastMembers() {
        final var castMembers = List.of(
                CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type())
        );

        this.castMemberRepository.saveAllAndFlush(castMembers.stream().map(CastMembersJPAEntity::from).toList());

        assertEquals(2, this.castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = castMembers.stream().map(CastMembersListOutput::from).toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = this.listCastMembersUseCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertTrue((expectedItems.size() == actualResult.items().size()) &&
                expectedItems.containsAll(actualResult.items()));

        verify(this.castMemberGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallListCastMembersAndIsEmpty_shouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<CastMembersListOutput>of();

        assertEquals(0, this.castMemberRepository.count());

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = this.listCastMembersUseCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItems, actualResult.items());

        verify(this.castMemberGateway, times(1)).findAll(any());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsError_shouldReturnException() {
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(this.castMemberGateway).findAll(any());

        final var actualException = assertThrows(IllegalStateException.class, () -> this.listCastMembersUseCase.execute(aQuery));

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.castMemberGateway, times(1)).findAll(any());
    }
}
