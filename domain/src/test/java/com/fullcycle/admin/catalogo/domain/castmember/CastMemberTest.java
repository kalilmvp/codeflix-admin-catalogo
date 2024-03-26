package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 3/25/24 10:09
 * @email kalilmvp@gmail.com
 */
public class CastMemberTest {

    @Test
    public void givenValidParams_whenCallNewMember_thenInstantiateNewCastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newCastMember(expectedName, expectedType);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
        assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewMember_shouldThrowException() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewMember_shouldReceiveNotification() {
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthMoreThan255_whenCallNewMember_shouldReceiveNotification() {
        final var expectedName = """
                Pensando mais a longo prazo, a estrutura atual da organização obstaculiza a apreciação da importância das novas proposições. Ainda assim, existem dúvidas a respeito de como a necessidade de renovação processual
                 auxilia a preparação e a composição de alternativas às soluções ortodoxas. Neste sentido, a consolidação das estruturas assume importantes posições no estabelecimento das direções preferenciais no sentido do 
                 progresso. Do mesmo modo, o desafiador cenário globalizado facilita a criação do sistema de formação de quadros que corresponde às necessidades. 
                As experiências acumuladas demonstram que a consulta aos diversos militantes garante a contribuição de um grupo importante na determinação das condições inegavelmente apropriadas.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNullType_whenCallNewMember_shouldReceiveNotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        actualMember.update(expectedName, expectedType);

        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithNameWithLenghtMoreThan255_shouldReceiveNotification() {
        final var expectedName = """
                Pensando mais a longo prazo, a estrutura atual da organização obstaculiza a apreciação da importância das novas proposições. Ainda assim, existem dúvidas a respeito de como a necessidade de renovação processual
                 auxilia a preparação e a composição de alternativas às soluções ortodoxas. Neste sentido, a consolidação das estruturas assume importantes posições no estabelecimento das direções preferenciais no sentido do 
                 progresso. Do mesmo modo, o desafiador cenário globalizado facilita a criação do sistema de formação de quadros que corresponde às necessidades. 
                As experiências acumuladas demonstram que a consulta aos diversos militantes garante a contribuição de um grupo importante na determinação das condições inegavelmente apropriadas.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithNullType_shouldReceiveNotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException = assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
