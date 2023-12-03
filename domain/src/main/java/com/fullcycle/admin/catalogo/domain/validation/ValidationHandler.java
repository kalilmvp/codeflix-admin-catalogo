package com.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

/**
 * @author kalil.peixoto
 * @date 2/16/23 19:24
 * @email kalilmvp@gmail.com
 */
public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anhandler);

    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        }
        return null;
    }

    interface Validation<T> {
        T validate();
    }
}
