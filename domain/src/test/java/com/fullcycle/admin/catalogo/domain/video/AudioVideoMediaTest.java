package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.UnitTests;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AudioVideoMediaTest extends UnitTests {

    @Test
    public void givenValidParams_whenCallNewAudioVideo_shouldReturnInstance() {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedCheckSum = "123";
        final var expectedName = "Banner.jpg";
        final var expectedRawLocation = "/images/123";
        final var expectedEncodedLocation = "/images/123";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var actualAudioVideo = AudioVideoMedia.with(expectedId, expectedCheckSum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        assertNotNull(actualAudioVideo);
        assertEquals(expectedCheckSum, actualAudioVideo.checksum());
        assertEquals(expectedName, actualAudioVideo.name());
        assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
        assertEquals(expectedStatus, actualAudioVideo.status());
    }

    @Test
    public void givenTwoAudioVideoWithSameCheckSumAndRawLocation_whenCallEquals_shouldReturnTrue() {
        // given
        final var expectedCheckSum = "123";
        final var expectedRawLocation = "/images/123";

        final var img1 = ImageMedia.with(expectedCheckSum, "Random", expectedRawLocation);
        final var img2 = ImageMedia.with(expectedCheckSum, "Simple", expectedRawLocation);

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, "123", "Random", "/images", "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "123", null, "/images", "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "123", "Random", null, "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "123", "Random", "/images", null, MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("id", "123", "Random", "/images", "/images", null));
    }
}
