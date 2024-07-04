package com.fullcycle.admin.catalogo.infrastructure.configuration.annotations;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author kalil.peixoto
 * @date 7/4/24 22:58
 * @email kalilmvp@gmail.com
 */
@Qualifier("VideoEvents")
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER})
public @interface VideoEvents {
}
