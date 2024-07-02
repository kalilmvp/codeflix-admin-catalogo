package com.fullcycle.admin.catalogo.domain.video;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 6/25/24 17:05
 * @email kalilmvp@gmail.com
 */
public enum VideoMediaType {
    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;

    public static Optional<VideoMediaType> of(final String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
