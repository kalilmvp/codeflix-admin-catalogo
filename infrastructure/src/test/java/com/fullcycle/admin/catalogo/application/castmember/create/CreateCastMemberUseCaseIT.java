package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
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
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase createCastMemberUseCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCommand_whenCallCreateCastMember_shouldReturnId() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = this.createCastMemberUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCastMember = this.castMemberRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertNotNull(actualCastMember.getCreatedAt());
        assertNotNull(actualCastMember.getUpdatedAt());
        assertEquals(actualCastMember.getCreatedAt(), actualCastMember.getUpdatedAt());

        verify(this.castMemberGateway, times(1)).create(any());
    }

    @Test
    public void givenInvalidName_whenCallCreateCastMember_shouldThrowNotificationException() {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // when
        final var actualOutput = assertThrows(NotificationException.class, () -> this.createCastMemberUseCase.execute(aCommand));

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
        final var actualOutput = assertThrows(NotificationException.class, () -> this.createCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedErrorCount, actualOutput.getErrors().size());
        assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());

        verify(this.castMemberGateway, times(0)).create(any());
    }
}
