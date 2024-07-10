package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 7/10/24 10:37
 * @email kalilmvp@gmail.com
 */
public record UpdateVideoRequest(@JsonProperty("title") String title,
                                 @JsonProperty("description") String description,
                                 @JsonProperty("year_launched") Integer yearLaunched,
                                 @JsonProperty("duration") Double duration,
                                 @JsonProperty("rating") String rating,
                                 @JsonProperty("opened") Boolean opened,
                                 @JsonProperty("published") Boolean published,
                                 @JsonProperty("categories_ids") Set<String> categories,
                                 @JsonProperty("genres_ids") Set<String> genres,
                                 @JsonProperty("cast_members_ids") Set<String> castMembers) {
}

