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
    ValidationHandler validate(Validation aValidation);
    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }



    interface Validation {
        void validate();
    }
}
