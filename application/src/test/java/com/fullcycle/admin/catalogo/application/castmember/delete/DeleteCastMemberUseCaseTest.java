package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase defaultDeleteCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    public void givenValidCastMemberId_whenCallDeleteCastMember_shouldDeleteCastMember() {
        // given
        final var aCastMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aCastMember.getId();

        doNothing().when(this.castMemberGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInValidCastMemberId_whenCallDeleteCastMember_shouldBeOk() {
        // given
        final var expectedId = CastMemberID.from("123");

        doNothing().when(this.castMemberGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenValidCastMemberId_whenCallDeleteCastMemberAndGatewayThrowsUnexpectedError_shoulReceiveException() {
        // given
        final var aCastMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aCastMember.getId();

        doThrow(new IllegalStateException("Gateway Error")).when(this.castMemberGateway).deleteById(any());

        // when
        assertThrows(IllegalStateException.class, () -> this.defaultDeleteCastMemberUseCase.execute(expectedId.getValue()));

        // then
        verify(this.castMemberGateway, times(1)).deleteById(expectedId);
    }
}
