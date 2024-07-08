package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:09
 * @email kalilmvp@gmail.com
 */
@Target(TYPE)
@Retention(RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoEncoderCompleted.class),
        @JsonSubTypes.Type(value = VideoEncoderError.class)
})
public @interface VideoResponseTypes {
}
