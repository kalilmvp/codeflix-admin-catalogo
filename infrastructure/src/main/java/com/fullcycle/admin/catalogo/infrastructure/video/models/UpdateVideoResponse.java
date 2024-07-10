package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 7/10/24 10:37
 * @email kalilmvp@gmail.com
 */
public record UpdateVideoResponse(@JsonProperty("id") String id) {
}

