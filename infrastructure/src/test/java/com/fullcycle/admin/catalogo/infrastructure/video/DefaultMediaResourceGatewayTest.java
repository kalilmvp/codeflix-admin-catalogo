package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.mediaType;
import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.resource;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        this.storageService().reset();
    }

    @Test
    public void testInjections() {
        assertNotNull(this.mediaResourceGateway);
        assertInstanceOf(DefaultMediaResourceGateway.class, this.mediaResourceGateway);
        assertNotNull(this.storageService);
        assertInstanceOf(InMemoryStorageService.class, this.storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedRawLocation = "";

        final var actualMedia =
                this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.rawLocation());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());
        assertEquals(expectedStatus, actualMedia.status());
        assertEquals(expectedRawLocation, actualMedia.encodedLocation());

        final var actualStored = this.storageService().storage().get(expectedLocation);
        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        final var actualMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.location());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = this.storageService().storage().get(expectedLocation);
        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        // given
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(tb -> this.storageService().store(tb, resource(mediaType())));
        expectedValues.forEach(tb -> this.storageService().store(tb, resource(mediaType())));

        assertEquals(5, this.storageService().storage().size());

        // when
        this.mediaResourceGateway.clearResources(videoOne);

        // then
        assertEquals(2, this.storageService().storage().size());

        final var actualKeys = this.storageService().storage().keySet();

        assertTrue(expectedValues.size() == actualKeys.size() &&
                expectedValues.containsAll(actualKeys));
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) this.storageService;
    }
}
