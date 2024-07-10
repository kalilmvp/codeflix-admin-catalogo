package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoResponse;
import com.fullcycle.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 7/8/24 16:39
 * @email kalilmvp@gmail.com
 */
@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoController(final CreateVideoUseCase createVideoUseCase,
                           final UpdateVideoUseCase updateVideoUseCase,
                           final GetVideoByIdUseCase getVideoByIdUseCase,
                           final DeleteVideoUseCase deleteVideoUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(final String aTitle,
                                        final String aDescription,
                                        final Integer aYearLaunched,
                                        final Double aDuration,
                                        final String aRating,
                                        final Boolean wasOpened,
                                        final Boolean wasPublished,
                                        final Set<String> categories,
                                        final Set<String> genres,
                                        final Set<String> castMembers,
                                        final MultipartFile aVideoFile,
                                        final MultipartFile aTrailerFile,
                                        final MultipartFile aBannerFile,
                                        final MultipartFile aThumbFile,
                                        final MultipartFile aThumbHalfFile) {

        final var aCmd = CreateVideoCommand.with(aTitle,
                aDescription,
                aYearLaunched,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                this.resourceOf(aVideoFile),
                this.resourceOf(aTrailerFile),
                this.resourceOf(aBannerFile),
                this.resourceOf(aThumbFile),
                this.resourceOf(aThumbHalfFile));

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/".concat(output.id())))
                .body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers());

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/".concat(output.id())))
                .body(output);
    }

    @Override
    public ResponseEntity<?> updateVideo(final String id,
                                         final UpdateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers());

        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity
                .ok()
                .header("Location", "/videos/".concat(output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public VideoResponse getById(String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public void deleteById(String anId) {
        this.deleteVideoUseCase.execute(anId);

    }

    private Resource resourceOf(MultipartFile aMultiPartFile) {
        if (aMultiPartFile == null) return null;

        try {
            return Resource.with(
                    HashingUtils.checksum(aMultiPartFile.getBytes()),
                    aMultiPartFile.getBytes(),
                    aMultiPartFile.getContentType(),
                    aMultiPartFile.getOriginalFilename());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
