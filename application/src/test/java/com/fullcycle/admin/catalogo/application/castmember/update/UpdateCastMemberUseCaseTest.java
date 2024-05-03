package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase defaultUpdateCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallUpdateCastMember_shouldReturnCastMemberId() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        when(this.castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultUpdateCastMemberUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(this.castMemberGateway, times(1)).update(argThat(anUpdatedCastMember ->
                Objects.equals(expectedName, anUpdatedCastMember.getName()) &&
                        Objects.equals(expectedType, anUpdatedCastMember.getType()) &&
                        Objects.nonNull(anUpdatedCastMember.getId()) &&
                        Objects.nonNull(aCastMember.getCreatedAt()) &&
                        Objects.equals(aCastMember.getCreatedAt(), anUpdatedCastMember.getCreatedAt()) &&
                        Objects.nonNull(anUpdatedCastMember.getUpdatedAt()) &&
                        aCastMember.getUpdatedAt().isBefore(anUpdatedCastMember.getUpdatedAt())));
    }

    @Test
    public void givenAnInvalidName_whenCallUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedId = aCastMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.defaultUpdateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.castMemberGateway).findById(expectedId);
        verify(this.castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnEmptyName_whenCallUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedId = aCastMember.getId();
        final String expectedName = " ";
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.defaultUpdateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.castMemberGateway).findById(expectedId);
        verify(this.castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aCastMember)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.defaultUpdateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.castMemberGateway).findById(expectedId);
        verify(this.castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenACastMemberThatDoesNotExist__whenCallUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        when(this.castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.defaultUpdateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.castMemberGateway).findById(expectedId);
        verify(this.castMemberGateway, times(0)).update(any());
    }
}
