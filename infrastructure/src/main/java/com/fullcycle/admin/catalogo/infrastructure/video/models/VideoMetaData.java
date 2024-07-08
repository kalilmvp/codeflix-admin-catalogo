package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:12
 * @email kalilmvp@gmail.com
 */
public record VideoMetaData(
        @JsonProperty("encoded_video_folder") String encodedVideoFolder,
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
