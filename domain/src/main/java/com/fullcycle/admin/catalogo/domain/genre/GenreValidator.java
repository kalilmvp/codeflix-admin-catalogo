package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

/**
 * @author kalil.peixoto
 * @date 11/29/23 00:26
 * @email kalilmvp@gmail.com
 */
public class GenreValidator extends Validator {
    private static final int INT_NAME_MIN_LENGTH = 1;
    private static final int INT_NAME_MAX_LENGTH = 255;
    private final Genre genre;

    protected GenreValidator(final Genre aGenre, ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        this.chackNameConstraints();
    }


    private void chackNameConstraints() {
        final var name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > INT_NAME_MAX_LENGTH || length < INT_NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }
    }
}
