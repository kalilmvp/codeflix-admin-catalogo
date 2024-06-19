package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 12/2/23 12:10
 * @email kalilmvp@gmail.com
 */
public class CreateCastMemberUseCaseTest extends UseCaseTest  {

    @InjectMocks
    private DefaultCreateCastMemberUseCase defaultCreateCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallCreateCastMember_shouldReturnId() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        when(this.castMemberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.defaultCreateCastMemberUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.castMemberGateway, times(1)).create(argThat(aCastMember ->
                Objects.equals(expectedName, aCastMember.getName()) &&
                        Objects.equals(expectedType, aCastMember.getType()) &&
                        Objects.nonNull(aCastMember.getId()) &&
                        Objects.nonNull(aCastMember.getCreatedAt()) &&
                        Objects.nonNull(aCastMember.getUpdatedAt())))   ;
    }

    @Test
    public void givenInvalidName_whenCallCreateCastMember_shouldThrowNotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = assertThrows(NotificationException.class, () -> this.defaultCreateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedErrorCount, actualOutput.getErrors().size());
        assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());

        verify(this.castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidType_whenCallCreateCastMember_shouldThrowNotificationException() {
        // given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = assertThrows(NotificationException.class, () -> this.defaultCreateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedErrorCount, actualOutput.getErrors().size());
        assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());

        verify(this.castMemberGateway, times(0)).create(any());
    }
}
