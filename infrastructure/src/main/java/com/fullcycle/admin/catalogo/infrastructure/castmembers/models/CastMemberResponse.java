package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 11/13/23 22:50
 * @email kalilmvp@gmail.com
 */
public record CastMemberResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt) {
}
