package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:05
 * @email kalilmvp@gmail.com
 */
@JsonTypeName("COMPLETED")
public record VideoEncoderCompleted(
        @JsonProperty("id") String id,
        @JsonProperty("output_bucket_path") String outputBucket,
        @JsonProperty("video") VideoMetaData video
) implements VideoEncoderResult {
    private static final String COMPLETED = "COMPLETED";

    @Override
    public String getStatus() {
        return COMPLETED;
    }
}
