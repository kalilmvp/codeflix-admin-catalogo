package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

/**
 * @author kalil.peixoto
 * @date 2/16/23 19:38
 * @email kalilmvp@gmail.com
 */
public class CastMemberValidator extends Validator {

    private static final int INT_NAME_MAX_LENGTH = 255;
    private static final int INT_NAME_MIN_LENGTH = 3;
    private final CastMember castMember;

    public CastMemberValidator(final CastMember aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.castMember = aCategory;
    }

    @Override
    public void validate() {
        this.chackNameConstraints();
        this.chackTypeConstraints();
    }

    private void chackNameConstraints() {
        final var name = this.castMember.getName();
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
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }

    private void chackTypeConstraints() {
        final var type = this.castMember.getType();
        if (type == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
            return;
        }
    }
}
