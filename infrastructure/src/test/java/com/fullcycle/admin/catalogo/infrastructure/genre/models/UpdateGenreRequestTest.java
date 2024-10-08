package com.fullcycle.admin.catalogo.infrastructure.genre.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

/**
 * @author kalil.peixoto
 * @date 11/19/23 21:59
 * @email kalilmvp@gmail.com
 */
@JacksonTest
public class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedCategory = "`12";
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "categories_id": ["%s"],
                    "is_active": %s
                }
                """.formatted(expectedName, expectedCategory, expectedIsActive);
        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
