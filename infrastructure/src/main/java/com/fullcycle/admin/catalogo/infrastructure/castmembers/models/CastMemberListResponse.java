package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 11/13/23 22:50
 * @email kalilmvp@gmail.com
 */
public record CastMemberListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") String createdAt) {
}
