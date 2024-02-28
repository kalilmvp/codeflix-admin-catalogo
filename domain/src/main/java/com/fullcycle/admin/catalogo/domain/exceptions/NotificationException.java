package com.fullcycle.admin.catalogo.domain.exceptions;

import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

/**
 * @author kalil.peixoto
 * @date 11/29/23 00:30
 * @email kalilmvp@gmail.com
 */
public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
