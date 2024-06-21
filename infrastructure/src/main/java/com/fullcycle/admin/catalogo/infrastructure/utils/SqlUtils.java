package com.fullcycle.admin.catalogo.infrastructure.utils;

/**
 * @author kalil.peixoto
 * @date 3/18/23 20:23
 * @email kalilmvp@gmail.com
 */
public final class SqlUtils {

    private SqlUtils() {}

    public static String like(final String term) {
        if (term == null) return null;
        return "%" + term + "%";
    }

    public static String upper(final String term) {
        if (term == null) return null;
        return term.toUpperCase();
    }
}
