package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:46
 * @email kalilmvp@gmail.com
 */
public record VideoListResponse(@JsonProperty("id") String id,
                                @JsonProperty("title") String title,
                                @JsonProperty("description") String description,
                                @JsonProperty("created_at") Instant createdAt,
                                @JsonProperty("updated_at") Instant updatedAt) {
}
