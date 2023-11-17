package com.fullcycle.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 10/17/23 23:13
 * @email kalilmvp@gmail.com
 */
public record UpdateCategoryRequest(@JsonProperty("name") String name,
                                    @JsonProperty("description") String description,
                                    @JsonProperty("is_active") Boolean active) {
}
