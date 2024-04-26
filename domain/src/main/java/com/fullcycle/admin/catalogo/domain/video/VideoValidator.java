package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

/**
 * @author kalil.peixoto
 * @date 11/29/23 00:26
 * @email kalilmvp@gmail.com
 */
public class VideoValidator extends Validator {
    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4_000;
    private final Video video;

    protected VideoValidator(final Video aVideo, ValidationHandler aHandler) {
        super(aHandler);
        this.video = aVideo;
    }

    @Override
    public void validate() {
        this.checkTitleConstraints();
        this.checkDescriptionConstraints();
        this.checkLauchedAtConstraints();
        this.checkRatingsConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        final int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error(String.format("'title' must be between 1 and %d characters", TITLE_MAX_LENGTH)));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error(String.format("'description' must be between 1 and %d characters", DESCRIPTION_MAX_LENGTH)));
        }
    }

    private void checkLauchedAtConstraints() {
        final var launchedAt = this.video.getLaunchedAt();
        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingsConstraints() {
        final var ratings = this.video.getRating();
        if (ratings == null) {
            this.validationHandler().append(new Error("'ratings' should not be null"));
        }
    }
}
