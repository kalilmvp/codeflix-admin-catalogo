package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public VideoController(CreateVideoUseCase createVideoUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
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
