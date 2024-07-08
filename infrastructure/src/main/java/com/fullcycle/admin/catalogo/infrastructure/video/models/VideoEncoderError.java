package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:05
 * @email kalilmvp@gmail.com
 */
@JsonTypeName("ERROR")
public record VideoEncoderError(
        @JsonProperty("message") VideoMessage message,
        @JsonProperty("error") String error
) implements VideoEncoderResult {
    private static final String ERROR = "ERROR";

    @Override
    public String getStatus() {
        return ERROR;
    }
}
