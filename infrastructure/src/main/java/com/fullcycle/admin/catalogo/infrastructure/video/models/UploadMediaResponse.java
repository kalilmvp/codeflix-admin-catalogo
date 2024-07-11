package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:46
 * @email kalilmvp@gmail.com
 */
public record UploadMediaResponse(@JsonProperty("video_id") String videoId,
                                  @JsonProperty("media_type") VideoMediaType mediaType) {
}
