package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageServiceTest {

    private InMemoryStorageService inMemoryStorageService = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        this.inMemoryStorageService.reset();
    }

    @Test
    public void givenValidResource_whenCallStore_shouldStoreIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        // when
        this.inMemoryStorageService.storage().put(expectedName, expectedResource);

        // then
        assertEquals(expectedResource, this.inMemoryStorageService.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallGet_shouldRetrieveIt() {
        // given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        this.inMemoryStorageService.storage().put(expectedName, expectedResource);

        // when
        final var actualResource = this.inMemoryStorageService.get(expectedName).get();

        // then
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInValidResource_whenCallGet_shouldReturnEmpty() {
        // given
        final var expectedName = IdUtils.uuid();

        // when
        final var actualResource = this.inMemoryStorageService.get(expectedName);

        // then
        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallList_shouldRetrieveAll() {
        // given
        final var expectedNames = List.of(
                "video_".concat(IdUtils.uuid()),
                "video_".concat(IdUtils.uuid()),
                "video_".concat(IdUtils.uuid())
        );

        final var all = new ArrayList<>(expectedNames);
        all.add("image_".concat(IdUtils.uuid()));
        all.add("image_".concat(IdUtils.uuid()));

        all.forEach(name -> this.inMemoryStorageService.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, this.inMemoryStorageService.storage().size());

        // when
        final var actualResult = this.inMemoryStorageService.list("video");

        // then
        assertTrue(expectedNames.size() == actualResult.size() &&
                expectedNames.containsAll(actualResult));
    }

    @Test
    public void givenValidNames_whenCallDelete_shouldDeleteAll() {
        // given
        final var videos = List.of(
                "video_".concat(IdUtils.uuid()),
                "video_".concat(IdUtils.uuid()),
                "video_".concat(IdUtils.uuid())
        );

        final var expectedÑames = Set.of(
                            "image_".concat(IdUtils.uuid()),
                            "image_".concat(IdUtils.uuid()));

        final var all = new ArrayList<>(videos);
        all.addAll(expectedÑames);

        all.forEach(name -> this.inMemoryStorageService.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        assertEquals(5, this.inMemoryStorageService.storage().size());

        // when
        this.inMemoryStorageService.deleteAll(videos);

        // then
        assertEquals(2, this.inMemoryStorageService.storage().size());
        assertTrue(expectedÑames.size() == this.inMemoryStorageService.storage().keySet().size() &&
                expectedÑames.containsAll(this.inMemoryStorageService.storage().keySet()));
    }
}
