package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMembersJPAEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/10/23 19:42
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

    @Autowired
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidId_whenCallGetCastMemberById_shouldReturnCastMember() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCastMember = CastMember.newCastMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var actualCastMember = this.getCastMemberByIdUseCase.execute(expectedId.getValue());

        assertNotNull(actualCastMember);
        assertEquals(expectedId.getValue(), actualCastMember.id());
        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType, actualCastMember.type());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.createdAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.updatedAt());

        verify(this.castMemberGateway, times(1)).findById(expectedId);
    }

    @Test
    public void givenAValidId_whenCallGetCastMemberByIdAndDoesNotExist_shouldReturnNotFound() {
        final var expectedId = CastMemberID.from("123");
        final var exceptedErrorMessage = "CastMember with ID 123 was not found";

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type())));

        assertEquals(1, this.castMemberRepository.count());

        final var actualException = assertThrows(NotFoundException.class, () -> this.getCastMemberByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());

        assertEquals(1, this.castMemberRepository.count());
    }
}
