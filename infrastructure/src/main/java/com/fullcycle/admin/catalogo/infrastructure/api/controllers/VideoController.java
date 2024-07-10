package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
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
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    public VideoController(CreateVideoUseCase createVideoUseCase,
                           GetVideoByIdUseCase getVideoByIdUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
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
    public VideoResponse getById(String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
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
