package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;


/**
 * @author kalil.peixoto
 * @date 3/8/23 20:56
 * @email kalilmvp@gmail.com
 */
public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        CastMember aCastMember =
                this.castMemberGateway.findById(anId)
                        .orElseThrow(notFound(anId));

        Notification notification = Notification.create();
        notification.validate(() -> aCastMember.update(aName, aType));

        if (notification.hasErrors()) {
            this.notify(anId, notification);
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(aCastMember));
    }

    private static void notify(Identifier aIdentifier, Notification notification) {
        throw new NotificationException("Could not update CastMember aggregate %s".formatted(aIdentifier.getValue()), notification);
    }

    private Supplier<DomainException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
