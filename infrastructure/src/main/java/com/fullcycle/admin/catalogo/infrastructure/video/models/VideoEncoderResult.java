package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:03
 * @email kalilmvp@gmail.com
 */

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "status")
@VideoResponseTypes
public sealed interface VideoEncoderResult permits VideoEncoderCompleted, VideoEncoderError {
    String getStatus();
}
