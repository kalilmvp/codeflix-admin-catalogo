package com.fullcycle.admin.catalogo.domain.exceptions;

/**
 * @author kalil.peixoto
 * @date 2/16/23 19:54
 * @email kalilmvp@gmail.com
 */
public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(final String message) {
        this(message, null);
    }

    public NoStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
