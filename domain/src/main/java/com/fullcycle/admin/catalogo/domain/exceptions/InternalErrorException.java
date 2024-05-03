package com.fullcycle.admin.catalogo.domain.exceptions;

/**
 * @author kalil.peixoto
 * @date 5/1/24 22:50
 * @email kalilmvp@gmail.com
 */
public class InternalErrorException extends NoStackTraceException {

    public InternalErrorException(final String aMessage, final Throwable t) {
        super(aMessage, t);
    }

    public static InternalErrorException with(final String aMessage, final Throwable t) {
        return new InternalErrorException(aMessage, t);
    }
}
