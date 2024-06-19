package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCastMemberRequestTest {

    @Autowired
    private JacksonTester<UpdateCastMemberRequest> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var updateCastMemberRequest = new UpdateCastMemberRequest(
                expectedName,
                expectedType
        );

        final var actualJson = this.json.write(updateCastMemberRequest);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType.name());
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var json = """
                {
                    "name": "%s",
                    "type": "%s"
                }
                """.formatted(expectedName, expectedType);
        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}
