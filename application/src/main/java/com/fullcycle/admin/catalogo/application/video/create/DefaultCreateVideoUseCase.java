package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:56
 * @email kalilmvp@gmail.com
 */
public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultCreateVideoUseCase(final CategoryGateway categoryGateway,
                                     final GenreGateway genreGateway,
                                     final CastMemberGateway castMemberGateway,
                                     final VideoGateway videoGateway, MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var castMembers = toIdentifier(aCommand.castMembers(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(this.validateCategories(categories));
        notification.append(this.validateGenres(genres));
        notification.append(this.validateCastMembers(castMembers));

        final var aVideo = Video.newVideo(aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aRating,
                aCommand.opened(),
                aCommand.published(),
                categories,
                genres,
                castMembers);

        aVideo.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create aggregate Video", notification);
        }

        return CreateVideoOutput.from(this.create(aCommand, aVideo));
    }

    private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();
        try {
            final var aVideoMedia = aCommand.getVideo().map(it -> this.mediaResourceGateway.storeAudioVideo(anId, it)).orElse(null);
            final var aTrailerMedia = aCommand.getTrailer().map(it -> this.mediaResourceGateway.storeAudioVideo(anId, it)).orElse(null);
            final var aBannerMedia = aCommand.getBanner().map(it -> this.mediaResourceGateway.storeImage(anId, it)).orElse(null);
            final var aThumbnailMedia = aCommand.getThumbnail().map(it -> this.mediaResourceGateway.storeImage(anId, it)).orElse(null);
            final var aThumbnailHalfMedia = aCommand.getThumbnailHalf().map(it -> this.mediaResourceGateway.storeImage(anId, it)).orElse(null);

            return this.videoGateway.create(aVideo
                    .setVideo(aVideoMedia)
                    .setTrailer(aTrailerMedia)
                    .setBanner(aBannerMedia)
                    .setThumbnail(aThumbnailMedia)
                    .setThumbnailHalf(aThumbnailHalfMedia));
        } catch (final Throwable t) {
            this.mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with("An error on create video ocurred [videoId:%s]".formatted(anId.getValue()), t);
        }
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return this.validateAggregate("categories", ids, this.categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return this.validateAggregate("genres", ids, this.genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> ids) {
        return this.validateAggregate("castMembers", ids, this.castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(final String aggregate,
                                                                       final Set<T> ids,
                                                                       final Function<Iterable<T>, List<T>> existsByIds) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            // after removing,what's left are the ones that doesn't exist at the db
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }
}
