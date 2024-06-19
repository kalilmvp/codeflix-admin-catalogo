package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMembersJPAEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCastMemberId_whenCallDeleteCastMember_shouldDeleteCastMember() {
        // given
        final var aCastMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aCastMember.getId();

        final var aCastMemberTwo = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));
        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMemberTwo));

        assertEquals(2, this.castMemberRepository.count());

        // when
        assertDoesNotThrow(() -> this.deleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);

        assertEquals(1, this.castMemberRepository.count());
        assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        assertTrue(this.castMemberRepository.existsById(aCastMemberTwo.getId().getValue()));
    }

    @Test
    public void givenInValidCastMemberId_whenCallDeleteCastMember_shouldBeOk() {
        // given
        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type())));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = CastMemberID.from("123");

        // when
        assertDoesNotThrow(() -> this.deleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);

        assertEquals(1, this.castMemberRepository.count());
    }

    @Test
    public void givenValidCastMemberId_whenCallDeleteCastMemberAndGatewayThrowsUnexpectedError_shoulReceiveException() {
        // given
        final var aCastMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aCastMember.getId();

        doThrow(new IllegalStateException("Gateway Error")).when(this.castMemberGateway).deleteById(any());

        // when
        assertThrows(IllegalStateException.class, () -> this.deleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);

        assertEquals(1, this.castMemberRepository.count());
    }
}
