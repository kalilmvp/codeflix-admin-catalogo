package com.fullcycle.admin.catalogo.e2e.castmember;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author kalil.peixoto
 * @date 4/1/24 23:21
 * @email kalilmvp@gmail.com
 */
@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withUsername("root")
                    .withPassword("123456")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mock() {
        return this.mockMvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var actualId = this.givenACastMember(expectedName, expectedType);

        final var actualCastMember = this.castMemberRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertNotNull(actualCastMember.getCreatedAt());
        assertNotNull(actualCastMember.getUpdatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingNewCastMemberWithInvalidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";

        this.givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThroughAllMembers() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        this.givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        this.givenACastMember("Steven Spielberg", CastMemberType.DIRECTOR);
        this.givenACastMember("Jackie Chan", CastMemberType.ACTOR);

        this.listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Jackie Chan")));

        this.listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Steven Spielberg")));

        this.listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchThroughAllMembers() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        this.givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        this.givenACastMember("Steven Spielberg", CastMemberType.DIRECTOR);
        this.givenACastMember("Jackie Chan", CastMemberType.ACTOR);

        this.listCastMembers(0, 1, "vin")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllMembersByNameDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        this.givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        this.givenACastMember("Steven Spielberg", CastMemberType.DIRECTOR);
        this.givenACastMember("Jackie Chan", CastMemberType.ACTOR);

        this.listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Steven Spielberg")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Jackie Chan")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualID = this.givenACastMember(expectedName, expectedType);

        final var actualCastMember = this.retrieveACastMember(actualID);

        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType.name(), actualCastMember.type());
        assertNotNull(actualCastMember.createdAt());
        assertNotNull(actualCastMember.updatedAt());
        assertEquals(actualCastMember.createdAt(), actualCastMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFound() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        this.retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualID = this.givenACastMember("vin d", CastMemberType.DIRECTOR);

        this.updateACastMember(actualID, expectedName, expectedType)
                .andExpect(status().isOk());

        final var actualCastMember = this.retrieveACastMember(actualID);

        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType.name(), actualCastMember.type());
        assertNotNull(actualCastMember.createdAt());
        assertNotNull(actualCastMember.updatedAt());
        assertNotEquals(actualCastMember.createdAt(), actualCastMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingWithInvalidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualID = this.givenACastMember("vin d", CastMemberType.DIRECTOR);

        this.updateACastMember(actualID, expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualID = this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        assertEquals(2, this.castMemberRepository.count());

        this.deleteACastMember(actualID)
                .andExpect(status().isNoContent());

        assertEquals(1, this.castMemberRepository.count());

        assertFalse(this.castMemberRepository.existsById(actualID.getValue()));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberWithInvalidIdentifier() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, this.castMemberRepository.count());

        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        this.givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        assertEquals(2, this.castMemberRepository.count());

        this.deleteACastMember(CastMemberID.from("123"))
                .andExpect(status().isNoContent());

        assertEquals(2, this.castMemberRepository.count());
    }
}
