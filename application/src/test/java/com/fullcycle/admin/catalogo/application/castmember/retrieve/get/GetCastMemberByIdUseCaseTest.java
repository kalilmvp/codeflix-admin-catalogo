package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/10/23 19:42
 * @email kalilmvp@gmail.com
 */
public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase defaultGetCastMemberByIdUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallGetCastMemberById_shouldReturnCastMember() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCastMember = CastMember.newCastMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        final var actualCastMember = this.defaultGetCastMemberByIdUseCase.execute(expectedId.getValue());

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

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> this.defaultGetCastMemberByIdUseCase.execute(expectedId.getValue()));

        assertEquals(exceptedErrorMessage, actualException.getMessage());
    }
}
