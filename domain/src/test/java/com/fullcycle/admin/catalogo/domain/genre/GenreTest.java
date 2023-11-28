package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author kalil.peixoto
 * @date 11/28/23 12:09
 * @email kalilmvp@gmail.com
 */
public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_thenInstantiateGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveError() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLenghtGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveError() {
        final var expectedName = """
                Pensando mais a longo prazo, a estrutura atual da organização obstaculiza a apreciação da importância das novas proposições. Ainda assim, existem dúvidas a respeito de como a necessidade de renovação processual
                 auxilia a preparação e a composição de alternativas às soluções ortodoxas. Neste sentido, a consolidação das estruturas assume importantes posições no estabelecimento das direções preferenciais no sentido do 
                 progresso. Do mesmo modo, o desafiador cenário globalizado facilita a criação do sistema de formação de quadros que corresponde às necessidades. 
                As experiências acumuladas demonstram que a consulta aos diversos militantes garante a contribuição de um grupo importante na determinação das condições inegavelmente apropriadas.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
