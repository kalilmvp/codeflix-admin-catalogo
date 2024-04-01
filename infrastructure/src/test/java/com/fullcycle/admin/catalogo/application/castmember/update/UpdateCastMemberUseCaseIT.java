package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
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
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCommand_whenCallUpdateCastMember_shouldReturnCastMemberId() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        // when
        final var actualOutput = this.updateCastMemberUseCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(this.castMemberGateway).findById(any());

        final var persistedCastMember = this.castMemberRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, persistedCastMember.getName());
        assertEquals(expectedType, persistedCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), persistedCastMember.getCreatedAt());
        assertTrue(aCastMember.getUpdatedAt().isBefore(persistedCastMember.getUpdatedAt()));

        verify(this.castMemberGateway).update(any());

    }

    @Test
    public void givenAnInvalidName_whenCallUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aCastMember = CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR);

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aCastMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.updateCastMemberUseCase.execute(aCommand));

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

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aCastMember.getId();
        final String expectedName = " ";
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.updateCastMemberUseCase.execute(aCommand));

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

        this.castMemberRepository.saveAndFlush(CastMembersJPAEntity.from(aCastMember));

        assertEquals(1, this.castMemberRepository.count());

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.updateCastMemberUseCase.execute(aCommand));

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
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue()
                , expectedName
                , expectedType);

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.updateCastMemberUseCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.castMemberGateway).findById(expectedId);
        verify(this.castMemberGateway, times(0)).update(any());
    }
}
