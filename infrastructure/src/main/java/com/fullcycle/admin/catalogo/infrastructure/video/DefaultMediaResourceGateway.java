package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

/**
 * @author kalil.peixoto
 * @date 6/27/24 17:42
 * @email kalilmvp@gmail.com
 */
@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {
    private final String fileNamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties storageProperties, final StorageService storageService) {
        this.fileNamePattern = storageProperties.fileNamePattern();
        this.locationPattern = storageProperties.locationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filepath = this.filePath(anId, videoResource);
        final var aResource = videoResource.resource();

        this.store(filepath, aResource);

        return AudioVideoMedia.with(
                aResource.checksum(),
                aResource.name(),
                filepath
        );
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource imageResource) {
        final var filepath = this.filePath(anId, imageResource);
        final var aResource = imageResource.resource();

        this.store(filepath, aResource);

        return ImageMedia.with(
                aResource.checksum(),
                aResource.name(),
                filepath
        );
    }

    @Override
    public void clearResources(final VideoID anId) {
        this.storageService.deleteAll(this.storageService.list(this.folder(anId)));
    }

    private String fileName(final VideoMediaType aType) {
        return this.fileNamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return this.locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoResource resource) {
        return this.folder(anId)
                .concat("/")
                .concat(this.fileName(resource.type()));
    }

    private void store(final String filePath, final Resource aResource) {
        this.storageService.store(filePath, aResource);
    }
}
