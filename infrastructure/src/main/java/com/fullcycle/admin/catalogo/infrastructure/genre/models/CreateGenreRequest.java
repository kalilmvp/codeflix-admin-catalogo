package com.fullcycle.admin.catalogo.infrastructure.genre.models;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateGenreRequest(
        @JsonProperty("name") String name,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("is_active") Boolean active
) {

    public List<String> categories() {
        return categories != null ? categories : Collections.emptyList();
    }

    public boolean isActive() {
        return active != null ? active : Boolean.TRUE;
    }

}
