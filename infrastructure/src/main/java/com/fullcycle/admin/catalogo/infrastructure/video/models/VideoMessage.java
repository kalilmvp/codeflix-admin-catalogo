package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:14
 * @email kalilmvp@gmail.com
 */
public record VideoMessage(
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
