package com.fullcycle.admin.catalogo.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author kalil.peixoto
 * @date 11/29/23 22:52
 * @email kalilmvp@gmail.com
 */
public final class InstantUtils {

    private InstantUtils() {
    }

    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
