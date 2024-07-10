package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:46
 * @email kalilmvp@gmail.com
 */
public record VideoResponse(@JsonProperty("id") String id,
                            @JsonProperty("title") String title,
                            @JsonProperty("description") String description,
                            @JsonProperty("year_launched") int yearLaunched,
                            @JsonProperty("duration") double duration,
                            @JsonProperty("opened") boolean opened,
                            @JsonProperty("published") boolean published,
                            @JsonProperty("rating") String rating,
                            @JsonProperty("created_at") Instant createdAt,
                            @JsonProperty("updated_at") Instant updatedAt,
                            @JsonProperty("categories_id") Set<String> categories,
                            @JsonProperty("genres_id") Set<String> genres,
                            @JsonProperty("cast_members_id") Set<String> castMembers,
                            @JsonProperty("video") AudioVideoMediaResponse video,
                            @JsonProperty("trailer") AudioVideoMediaResponse trailer,
                            @JsonProperty("banner") ImageMediaResponse banner,
                            @JsonProperty("thumbnail") ImageMediaResponse thumbnail,
                            @JsonProperty("thumbnail_half") ImageMediaResponse thumbnailHalf) {
}
