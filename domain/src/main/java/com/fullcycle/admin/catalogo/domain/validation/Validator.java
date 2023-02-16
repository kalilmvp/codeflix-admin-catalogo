package com.fullcycle.admin.catalogo.domain.validation;

/**
 * @author kalil.peixoto
 * @date 2/16/23 19:36
 * @email kalilmvp@gmail.com
 */
public abstract class Validator {

    private final ValidationHandler validationHandler;

    protected Validator(final ValidationHandler aHandler) {
        this.validationHandler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.validationHandler;
    }
}
