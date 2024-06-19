package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = CastMemberID.unique().getValue();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type().name();
        final var expectedCreatedAt = Instant.now().toString();

        final var castMemberListResponse = new CastMemberListResponse(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt
        );

        final var actualJson = this.json.write(castMemberListResponse);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt);
    }
}
