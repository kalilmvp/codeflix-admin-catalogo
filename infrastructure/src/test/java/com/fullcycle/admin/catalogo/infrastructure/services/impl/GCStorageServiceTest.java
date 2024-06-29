package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GCStorageServiceTest {

    private GCStorageService gcStorageService;
    private Storage storage;
    private final String bucket = "fc3_test";

    @BeforeEach
    public void setUp() {
        this.storage = mock(Storage.class);
        this.gcStorageService = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldPersitIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = this.mockBlob(expectedName, expectedResource);

        doReturn(blob)
            .when(this.storage).create(any(BlobInfo.class), any());

        // when
        this.gcStorageService.store(expectedName, expectedResource);

        // then
        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(this.storage, times(1))
                .create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedName, actualBlob.getBlobId().getName());
        assertEquals(expectedName, actualBlob.getName());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallGet_shouldRetrieveIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = this.mockBlob(expectedName, expectedResource);

        doReturn(blob)
                .when(this.storage).get(anyString(), anyString());

        // when
        final var actualResource = this.gcStorageService.get(expectedName).get();

        // then
        verify(this.storage, times(1))
                .get(eq(this.bucket), eq(expectedName));

        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInValidResource_whenCallGet_shouldReturnEmpty() {
        // given
        final var expectedName = IdUtils.uuid();

        doReturn(null)
                .when(this.storage).get(anyString(), anyString());

        // when
        final var actualResource = this.gcStorageService.get(expectedName);

        // then
        verify(this.storage, times(1))
                .get(eq(this.bucket), eq(expectedName));

        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallList_shouldRetrieveAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix.concat(IdUtils.uuid());
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedNameBanner = expectedPrefix.concat(IdUtils.uuid());
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        final var blobVideo = this.mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner = this.mockBlob(expectedNameBanner, expectedBanner);

        final var page = mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();
        doReturn(page).when(this.storage).list(anyString(), any());

        // when
        final var actualResource = this.gcStorageService.list(expectedPrefix);

        // then
        verify(this.storage, times(1))
                .list(eq(this.bucket), eq(prefix(expectedPrefix)));

        assertTrue(expectedResources.size() == actualResource.size() &&
                expectedResources.containsAll(actualResource));
    }

    @Test
    public void givenValidNames_whenCallDelete_shouldDeleteAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix.concat(IdUtils.uuid());
        final var expectedNameBanner = expectedPrefix.concat(IdUtils.uuid());

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        // when
        this.gcStorageService.deleteAll(expectedResources);

        // then
        final var captor = ArgumentCaptor.forClass(List.class);
        verify(this.storage, times(1))
                .delete(captor.capture());

        final var actualResources = ((List<BlobId>)captor.getValue())
                .stream()
                .map(BlobId::getName)
                .toList();

        assertTrue(
                actualResources.size() == expectedResources.size() &&
                        actualResources.containsAll(expectedResources));
    }

    private Blob mockBlob(final String name, final Resource resource) {
        final var mockBlob = mock(Blob.class);

        when(mockBlob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(mockBlob.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(mockBlob.getContent()).thenReturn(resource.content());
        when(mockBlob.getContentType()).thenReturn(resource.contentType());
        when(mockBlob.getName()).thenReturn(resource.name());

        return mockBlob;
    }
}
