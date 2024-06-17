package com.fullcycle.admin.catalogo.domain.utils;

import java.util.UUID;

/**
 * @author kalil.peixoto
 * @date 11/29/23 22:52
 * @email kalilmvp@gmail.com
 */
public final class IdUtils {

    private IdUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
