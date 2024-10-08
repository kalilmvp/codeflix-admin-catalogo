package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:56
 * @email kalilmvp@gmail.com
 */
public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUpdateVideoUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway, final CastMemberGateway castMemberGateway, final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(final UpdateVideoCommand aCommand) {
        final var aVideoId = VideoID.from(aCommand.id());

        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var castMembers = toIdentifier(aCommand.castMembers(), CastMemberID::from);

        final var aVideo = this.videoGateway.findById(aVideoId).orElseThrow(notFound(aVideoId));

        final var notification = Notification.create();
        notification.append(this.validateCategories(categories));
        notification.append(this.validateGenres(genres));
        notification.append(this.validateCastMembers(castMembers));

        aVideo.update(aCommand.title(), aCommand.description(), aLaunchYear, aCommand.duration(), aRating, aCommand.opened(), aCommand.published(), categories, genres, castMembers);

        aVideo.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not uodate aggregate Video", notification);
        }

        return UpdateVideoOutput.from(this.update(aCommand, aVideo));
    }

    private Video update(final UpdateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();
        try {
            final var aVideoMedia = aCommand.getVideo().map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, VIDEO))).orElse(null);
            final var aTrailerMedia = aCommand.getTrailer().map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, TRAILER))).orElse(null);
            final var aBannerMedia = aCommand.getBanner().map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, BANNER))).orElse(null);
            final var aThumbnailMedia = aCommand.getThumbnail().map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, THUMBNAIL))).orElse(null);
            final var aThumbnailHalfMedia = aCommand.getThumbnailHalf().map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, THUMBNAIL_HALF))).orElse(null);

            return this.videoGateway.update(aVideo
                    .updateVideoMedia(aVideoMedia)
                    .updateTrailerMedia(aTrailerMedia)
                    .updateBannerMedia(aBannerMedia)
                    .updateThumbnailMedia(aThumbnailMedia)
                    .updateThumbnailHalfMedia(aThumbnailHalfMedia));
        } catch (final Throwable t) {
            throw InternalErrorException.with("An error on update video ocurred [videoId:%s]".formatted(anId.getValue()), t);
        }
    }

    private Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = this.categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            // after removing,what's left are the ones that doesn't exist at the db
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream().map(CategoryID::getValue).collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
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

    private <T extends Identifier> ValidationHandler validateAggregate(final String aggregate, final Set<T> ids, final Function<Iterable<T>, List<T>> existsByIds) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            // after removing,what's left are the ones that doesn't exist at the db
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream().map(Identifier::getValue).collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }
}
